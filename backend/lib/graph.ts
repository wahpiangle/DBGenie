import { END, MemorySaver, START, StateGraph } from "@langchain/langgraph";
import { blockTables, checkUserQuery, evaluateSufficientInfo, generateErrorMessage, generateReadQueryResult, generateSqlQuery, GraphState, injectionPrevention, runQueryToDb } from "./nodes";

const graph = new StateGraph(GraphState)
    .addNode('generateSqlQuery', generateSqlQuery)
    .addNode('checkUserQuery', checkUserQuery)
    .addNode('blockTables', blockTables)
    .addNode('evaluateSufficientInfo', evaluateSufficientInfo)
    .addNode('runQueryToDb', runQueryToDb)
    .addNode('generateErrorMessage', generateErrorMessage)
    .addNode('injectionPrevention', injectionPrevention)
    .addNode('generateReadQueryResult', generateReadQueryResult)

graph.addEdge(START, 'generateSqlQuery')
graph.addEdge('generateSqlQuery', 'checkUserQuery')

graph.addEdge('generateErrorMessage', END)
const app = graph.compile({
    checkpointer: new MemorySaver(),
})