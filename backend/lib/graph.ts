import { END, START, StateGraph } from "@langchain/langgraph";
import { blockTables, checkAlterTableSchema, checkUserQueryOwnData, decideAlterSchema, decideToReject, evaluateSufficientInfo, generateErrorMessage, generateSqlQuery, GraphState, injectionPrevention, runQueryToDb } from "./nodes";

const graph = new StateGraph(GraphState)
    .addNode('generateSqlQuery', generateSqlQuery)
    .addNode('checkAlterTableSchema', checkAlterTableSchema)
    .addNode('blockTables', blockTables)
    .addNode('evaluateSufficientInfo', evaluateSufficientInfo)
    .addNode('runQueryToDb', runQueryToDb)
    .addNode('generateErrorMessage', generateErrorMessage)
    .addNode('injectionPrevention', injectionPrevention)
    .addNode('checkUserQueryOwnData', checkUserQueryOwnData);

graph.addEdge(START, 'generateSqlQuery')
graph.addEdge('generateSqlQuery', 'checkAlterTableSchema');
graph.addConditionalEdges('checkAlterTableSchema', decideAlterSchema, {
    'alter': 'injectionPrevention',
    'not alter': 'blockTables'
})
graph.addConditionalEdges('blockTables', decideToReject, {
    'accept': 'evaluateSufficientInfo',
    'reject': 'generateErrorMessage'
});
graph.addConditionalEdges('evaluateSufficientInfo', decideToReject, {
    'accept': 'checkUserQueryOwnData',
    'reject': 'generateErrorMessage'
});
graph.addConditionalEdges('checkUserQueryOwnData', decideToReject, {
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