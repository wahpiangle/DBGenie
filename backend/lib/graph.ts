import { END, START, StateGraph } from "@langchain/langgraph";
import { blockTables, checkUserQuery, decideToReject, evaluateSufficientInfo, generateErrorMessage, generateSqlQuery, GraphState, injectionPrevention, runQueryToDb } from "./nodes";

const graph = new StateGraph(GraphState)
    .addNode('generateSqlQuery', generateSqlQuery)
    .addNode('checkUserQuery', checkUserQuery)
    .addNode('blockTables', blockTables)
    .addNode('evaluateSufficientInfo', evaluateSufficientInfo)
    .addNode('runQueryToDb', runQueryToDb)
    .addNode('generateErrorMessage', generateErrorMessage)
    .addNode('injectionPrevention', injectionPrevention)

graph.addEdge(START, 'generateSqlQuery')
graph.addEdge('generateSqlQuery', 'blockTables');
graph.addConditionalEdges('blockTables', decideToReject, {
    'accept': 'evaluateSufficientInfo',
    'reject': 'generateErrorMessage'
});
graph.addConditionalEdges('evaluateSufficientInfo', decideToReject, {
    'accept': 'checkUserQuery',
    'reject': 'generateErrorMessage'
});
graph.addConditionalEdges('checkUserQuery', decideToReject, {
    'accept': 'injectionPrevention',
    'reject': 'generateErrorMessage'
});
graph.addConditionalEdges('injectionPrevention', decideToReject, {
    'accept': 'runQueryToDb',
    'reject': 'generateErrorMessage'
});
graph.addEdge('runQueryToDb', END);
graph.addEdge('generateErrorMessage', END);

const appGraph = graph.compile({
    // checkpointer: new MemorySaver(),
})

export default appGraph;