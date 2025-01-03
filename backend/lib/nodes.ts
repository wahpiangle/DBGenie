import { Annotation, END } from "@langchain/langgraph"
import { db, sqlQueryChain } from "./chatbot"
import { questionEvaluation, type QuestionEvaluatorOutput } from "./tools/questionEvaluator"
import { prisma } from "../prisma"
import { type User } from "@prisma/client"
import { userQueryChecker } from "./tools/userQueryChecker"
import { determineExecuteOrQuery } from "./tools/determineExecuteOrQuery"
import { injectionPreventionChecker } from "./tools/injectionPrevention"
import { tableColumnGenerator } from "./tools/tableColumnGenerator"
import { readQueryGenerator } from "./tools/readQueryGenerator"
import { ownDataChecker } from "./tools/ownDataChecker"
import successExecuteMessageGeneration from "./tools/successExecuteMessageGeneration"

const GraphState = Annotation.Root({
    question: Annotation<string>,
    generation: Annotation<string>,
    user: Annotation<User>,
    errorMessage: Annotation<string>,
    result: Annotation<string>
})

const generateSqlQuery = async (state: typeof GraphState.State): Promise<Partial<typeof GraphState.State>> => {
    console.log("Generating SQL query for question:", state.question)
    const generatedQuery = await sqlQueryChain.invoke({
        question: state.question
    })
    const extractSQL = (input: string) => {
        const regex = /```sql\n([\s\S]*?)\n```/;
        const match = input.match(regex);
        return match ? match[1] : input;
    };

    return { generation: extractSQL(generatedQuery) }
}

const checkUserQuery = async (state: typeof GraphState.State) => {
    // check if the user is updating or deleting a record that does not belong to them
    console.log("======== Checking if the user is updating or deleting a record that does not belong to them ========")
    const user = state.user
    const ids = await prisma.user.findUnique({
        where: {
            id: user.id
        },
        select: {
            Booking: {
                select: {
                    id: true
                }
            },
            maintenanceRequest: {
                select: {
                    id: true
                }
            },
            maintenanceRequestUpdate: {
                select: {
                    id: true
                }
            },
            Property: {
                select: {
                    id: true
                }
            },
        }
    })
    const response = await ownDataChecker.invoke({
        sql_statement: state.generation,
        user_json: JSON.stringify(ids)
    }) as { error: string, conflicted_tables: string[] }
    if (response.error) {
        return { errorMessage: "You are not allowed to access the specific data. Please try again." }
    }
}

const blockTables = async (state: typeof GraphState.State) => {
    console.log("====== Blocking tables ======")
    const userQuery = await userQueryChecker.invoke({
        blockedTables: ['Users', 'RentalBill', 'Payment', 'VerificationToken', 'Session'].join(', '),
        sql_statement: state.generation
    })
    if (userQuery.split(' ')[0].toLowerCase() === 'yes') {
        return { errorMessage: "You are not allowed to access the specific data. Please try again." }
    }
    return;
}

const injectionPrevention = async (state: typeof GraphState.State) => {
    console.log("====== Checking for SQL injection ======")
    const sqlQuery = state.generation
    const isMalicious = await injectionPreventionChecker.invoke({
        sql_statement: sqlQuery
    })
    if (isMalicious.split(' ')[0].toLowerCase() === 'yes') {
        return {
            errorMessage: "The query is invalid and cannot be processed. Please try again."
        }
    }
    return;
}

const evaluateSufficientInfo = async (state: typeof GraphState.State) => {
    // check if the user's query has sufficient information for the sql query
    console.log(`====== Evaluating if the user's query has sufficient information ======
        `)
    const hasSufficientInfo = await questionEvaluation.invoke({
        input: state.question,
        sql_statement: state.generation,
        table: tableColumnGenerator(db)
    }) as QuestionEvaluatorOutput

    if (hasSufficientInfo.evaluation === 'Insufficient') {
        console.log("====== QUERY INSUFFICIENT INFORMATION ======")
        return { errorMessage: hasSufficientInfo.feedback }
    } else {
        console.log("====== QUERY SUFFICIENT INFORMATION ======")
    }
    return
}

const runQueryToDb = async (state: typeof GraphState.State) => {
    console.log("Running query to database")
    const isQuery = await determineExecuteOrQuery.invoke({
        sql_statement: state.generation
    })
    if (isQuery.split(' ')[0].toLowerCase() === 'yes') {
        return {
            result: await readQueryGenerator.invoke({
                sql_statement: state.generation,
                result: JSON.stringify(await prisma.$queryRawUnsafe(state.generation))
            })
        }
    } else {
        prisma.$executeRawUnsafe(state.generation)
        return {
            result: await successExecuteMessageGeneration.invoke({
                sql_statement: state.generation
            })
        }
    }
}

const generateErrorMessage = async (state: typeof GraphState.State) => {
    console.log("Generating error message")
    console.log("The state is:", state)
    return { result: state.errorMessage }
}

const decideToReject = async (state: typeof GraphState.State) => {
    if (state.errorMessage) {
        return "reject"
    } else {
        return "accept"
    }
}

export {
    GraphState,
    generateSqlQuery,
    checkUserQuery,
    blockTables,
    evaluateSufficientInfo,
    runQueryToDb,
    generateErrorMessage,
    injectionPrevention,
    decideToReject
}