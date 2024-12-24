import { Annotation } from "@langchain/langgraph"
import { db, sqlQueryChain } from "./chatbot"
import { questionEvaluation } from "./tools/questionEvaluator"
import { prisma } from "../prisma"
import { type User } from "@prisma/client"
import { userQueryChecker } from "./tools/userQueryChecker"
import { determineExecuteOrQuery } from "./tools/determineExecuteOrQuery"
import { injectionPreventionChecker } from "./tools/injectionPrevention"
import { tableColumnGenerator } from "./tools/tableColumnGenerator"

const GraphState = Annotation.Root({
    question: Annotation<string>,
    generation: Annotation<string>,
    user: Annotation<User>,
    rejected: Annotation<boolean>,
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

const blockTables = async (state: typeof GraphState.State): Promise<Partial<typeof GraphState.State>> => {
    console.log("Blocking users from manipulating tables")
    const sqlQuery = state.generation
    const userQuery = await userQueryChecker.invoke({
        sql_statement: sqlQuery
    })
    // get the first word of the sql query
    const firstWord = userQuery.split(' ')[0].toLowerCase()
    if (firstWord === 'yes') {
        return { rejected: true }
    }
    return { rejected: false }
}

const injectionPrevention = async (state: typeof GraphState.State): Promise<Partial<typeof GraphState.State>> => {
    console.log("Checking for SQL injection attacks")
    const sqlQuery = state.generation
    const isMalicious = await injectionPreventionChecker.invoke({
        sql_statement: sqlQuery
    })
    if (isMalicious.split(' ')[0].toLowerCase() === 'yes') {
        return { rejected: true }
    }
    return { rejected: false }
}

const evaluateSufficientInfo = async (state: typeof GraphState.State) => {
    // check if the user's query has sufficient information for the sql query
    console.log("Checking if the user's query has sufficient information for the sql query")
    const hasSufficientInfo = questionEvaluation.invoke({
        input: state.question,
        sql_statement: state.generation,
        table: tableColumnGenerator(db)
    })
    return { hasSufficientInfo }
}

const runQueryToDb = async (state: typeof GraphState.State) => {
    console.log("Running query to database")
    const isQuery = await determineExecuteOrQuery.invoke({
        sql_statement: state.generation
    })
    if (isQuery.split(' ')[0].toLowerCase() === 'yes') {
        prisma.$queryRawUnsafe(state.generation)
    } else {
        prisma.$executeRawUnsafe(state.generation)
    }
}

export { GraphState, generateSqlQuery, checkUserQuery, blockTables, evaluateSufficientInfo, runQueryToDb }