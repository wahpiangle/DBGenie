import { Annotation } from "@langchain/langgraph"
import { db, sqlQueryChain } from "./chatbot"
import { questionEvaluation, type QuestionEvaluatorOutput } from "./tools/questionEvaluator"
import { prisma } from "../prisma"
import { type User } from "@prisma/client"
import { userQueryChecker } from "./tools/userQueryChecker"
import { determineExecuteOrQuery } from "./tools/determineExecuteOrQuery"
import { injectionPreventionChecker } from "./tools/injectionPrevention"
import { tableColumnGenerator } from "./tools/tableColumnGenerator"
import { readQueryGenerator } from "./tools/readQueryGenerator"

const GraphState = Annotation.Root({
    question: Annotation<string>,
    generation: Annotation<string>,
    user: Annotation<User>,
    rejected: Annotation<boolean>,
    errorMessage: Annotation<string>,
    result: Annotation<string>
})

const generateSqlQuery = async (state: typeof GraphState.State): Promise<Partial<typeof GraphState.State>> => {
    console.log("Generating SQL query for question:", state.question)
    const generatedQuery = await sqlQueryChain.invoke({
        question: state.question
    })
    return { generation: generatedQuery }
}

const checkUserQuery = async (state: typeof GraphState.State): Promise<Partial<typeof GraphState.State>> => {
    // check if the user is updating or deleting a record that does not belong to them
    console.log("Checking if the user is updating or deleting a record that does not belong to them")
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

    // check if the user is updating or deleting a record that does not belong to them
    const sqlQuery = state.generation

    return {}
}

const blockTables = async (state: typeof GraphState.State) => {
    console.log("Blocking users from manipulating tables")
    const sqlQuery = state.generation
    const userQuery = await userQueryChecker.invoke({
        blockedTables: ['users', 'RentalBill', 'Payment', 'VerificationToken', 'Session'].join(', '),
        sql_statement: sqlQuery
    })
    if (userQuery.split(' ')[0].toLowerCase() === 'yes') {
        state.errorMessage = "You are not allowed to access the specific data. Please try again."
        return { errorMessage: state.errorMessage }
    }
    return 'injectionPrevention';
}

const injectionPrevention = async (state: typeof GraphState.State) => {
    console.log("Checking for SQL injection attacks")
    const sqlQuery = state.generation
    const isMalicious = await injectionPreventionChecker.invoke({
        sql_statement: sqlQuery
    })
    if (isMalicious.split(' ')[0].toLowerCase() === 'yes') {
        state.errorMessage = "The query is invalid and cannot be processed. Please try again."
        return { errorMessage: state.errorMessage }
    }
    return 'evaluateSufficientInfo';
}

const evaluateSufficientInfo = async (state: typeof GraphState.State) => {
    // check if the user's query has sufficient information for the sql query
    console.log("Checking if the user's query has sufficient information for the sql query")
    const hasSufficientInfo = await questionEvaluation.invoke({
        input: state.question,
        sql_statement: state.generation,
        table: tableColumnGenerator(db)
    }) as QuestionEvaluatorOutput

    if (hasSufficientInfo.evaluation === 'Insufficient') {
        return { errorMessage: hasSufficientInfo.feedback }
    } else {
        return
    }
}

const runQueryToDb = async (state: typeof GraphState.State) => {
    console.log("Running query to database")
    const isQuery = await determineExecuteOrQuery.invoke({
        sql_statement: state.generation
    })
    if (isQuery.split(' ')[0].toLowerCase() === 'yes') {
        return {
            result: JSON.stringify(await prisma.$queryRawUnsafe(state.generation))
        }
    } else {
        prisma.$executeRawUnsafe(state.generation)
    }
}

const generateErrorMessage = async (state: typeof GraphState.State) => {
    console.log("Generating error message")
    return { result: state.errorMessage }
}

const generateReadQueryResult = async (state: typeof GraphState.State) => {
    console.log("Generating read query result")
    const response = await readQueryGenerator.invoke({
        sql_statement: state.generation,
        result: state.result
    })
    return { result: response }
}

export { GraphState, generateSqlQuery, checkUserQuery, blockTables, evaluateSufficientInfo, runQueryToDb, generateErrorMessage, injectionPrevention, generateReadQueryResult }