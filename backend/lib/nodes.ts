import { Annotation } from "@langchain/langgraph"
import { sqlQueryChain } from "./chatbot"
import { questionEvaluation } from "./tools/questionEvaluator"

const GraphState = Annotation.Root({
    question: Annotation<string>,
    generation: Annotation<string>,
})

const generateSqlQuery = async (state: typeof GraphState.State): Promise<Partial<typeof GraphState.State>> => {
    console.log("Generating SQL query for question:", state.question)
    const generatedQuery = await sqlQueryChain.invoke({
        question: state.question
    })
    return { generation: generatedQuery }
}

const evaluateSufficientInfo = async (state: typeof GraphState.State) => {
    // check if the user's query has sufficient information for the sql query
    console.log("Checking if the user's query has sufficient information for the sql query")
    const hasSufficientInfo = questionEvaluation.invoke({
        input: state.question,
        sql_statement: state.generation
    })
    return { hasSufficientInfo }
}

const runQueryToDb = async (state: typeof GraphState.State) => {
    console.log("Running query to database")
}