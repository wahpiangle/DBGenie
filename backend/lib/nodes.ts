import { Annotation, END } from "@langchain/langgraph"
import { db } from "./chatbot"
import { questionEvaluation, type QuestionEvaluatorOutput } from "./tools/questionEvaluator"
import { prisma } from "../prisma"
import { type user } from "@prisma/client"
import { userQueryChecker } from "./tools/userQueryChecker"
import { determineExecuteOrQuery } from "./tools/determineExecuteOrQuery"
import { injectionPreventionChecker } from "./tools/injectionPrevention"
import { createId } from '@paralleldrive/cuid2';
import { readQueryGenerator } from "./tools/readQueryGenerator"
import { ownDataChecker } from "./tools/ownDataChecker"
import successExecuteMessageGeneration from "./tools/successExecuteMessageGeneration"
import { SQLStatementGenerator } from "./tools/generateSQLStatement"
import { determineAlterSchema } from "./tools/determineAlterSchema"

const GraphState = Annotation.Root({
    question: Annotation<string>,
    generation: Annotation<string>,
    user: Annotation<user>,
    errorMessage: Annotation<string>,
    result: Annotation<string>,
    alterSchema: Annotation<boolean>
})

const generateSqlQuery = async (state: typeof GraphState.State): Promise<Partial<typeof GraphState.State>> => {
    console.log("Generating SQL query for question:", state.question)
    const generatedQuery = await SQLStatementGenerator.invoke({
        generated_id: createId(),
        database_schema: db.allTables,
        user_query: state.question,
        user_id: state.user.id
    })
    const extractSQL = (input: string) => {
        const regex = /```sql\n([\s\S]*?)\n```/;
        const match = input.match(regex);
        return match ? match[1] : input;
    };
    const removeThinkTag = (input: string) => {
        return input.replace(/<think>[\s\S]*?<\/think>/g, "")
    }
    console.log("Generated SQL query:", generatedQuery)
    console.log("Extracted SQL query:", removeThinkTag(extractSQL(generatedQuery)))
    return { generation: removeThinkTag(extractSQL(generatedQuery)) }
}

const checkAlterTableSchema = async (state: typeof GraphState.State) => {
    console.log("====== Checking if the user is trying to update the table schema ======")
    const response = await determineAlterSchema.invoke({
        sql_statement: state.generation
    });

    return ({
        alterSchema: (response.split(' ')[0].toLowerCase() === 'yes')
    });
}

const checkUserQueryOwnData = async (state: typeof GraphState.State) => {
    console.log("======== Checking if the user is updating or deleting a record that does not belong to them ========")
    const user = state.user
    const ids = await prisma.user.findUnique({
        where: {
            id: user.id
        },
        select: {
            booking: {
                select: {
                    id: true
                }
            },
            maintenance_request: {
                select: {
                    id: true
                }
            },
            maintenance_request_update: {
                select: {
                    id: true
                }
            },
            property: {
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
        blockedTables: ['Users', 'VerificationToken', 'Session'].join(', '),
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
    console.log(`====== Evaluating if the user's query has sufficient information ======`)
    const hasSufficientInfo = await questionEvaluation.invoke({
        input: state.question,
        sql_statement: state.generation,
        table: db.allTables
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
    console.log("Running query to database: ", state.generation)
    const isQuery = await determineExecuteOrQuery.invoke({
        sql_statement: state.generation
    })
    const removeThinkTag = (input: string) => {
        return input.replace(/<think>[\s\S]*?<\/think>/g, "")
    }
    try {

        if (isQuery.split(' ')[0].toLowerCase() === 'yes') {
            const queryResult = await prisma.$queryRawUnsafe(state.generation)
            console.log("Query result:", queryResult)
            return {
                result: removeThinkTag(await readQueryGenerator.invoke({
                    sql_statement: state.generation,
                    result: JSON.stringify(queryResult)
                }))
            }
        } else {
            const queryResult = await prisma.$executeRawUnsafe(state.generation)
            return {
                result: removeThinkTag(await successExecuteMessageGeneration.invoke({
                    sql_statement: state.generation
                }))
            }
        }
    }
    catch (e) {
        console.log("Error:", e)
        return {
            errorMessage: "The query is invalid and cannot be processed. Please try again."
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

const decideAlterSchema = async (state: typeof GraphState.State) => {
    if (state.alterSchema) {
        return "alter"
    } else {
        return "not alter"
    }
}

export {
    GraphState,
    generateSqlQuery,
    checkAlterTableSchema,
    checkUserQueryOwnData,
    blockTables,
    evaluateSufficientInfo,
    runQueryToDb,
    generateErrorMessage,
    injectionPrevention,
    decideToReject,
    decideAlterSchema,
}