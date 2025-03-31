import { END, START, StateGraph } from "@langchain/langgraph";
import { blockTables, blockUserFromAlteringSchema, checkAlterTableSchema, checkUserQueryOwnData, decideAlterSchema, decideToReject, determineUserQueryIsValid, evaluateSufficientInfo, generateErrorMessage, generateSqlQuery, GraphState, injectionPrevention, runQueryToDb } from "./nodes";
import checkpointer from "./checkpointer";

const graph = new StateGraph(GraphState)
    .addNode('generateSqlQuery', generateSqlQuery)
    .addNode('determineUserQueryIsValid', determineUserQueryIsValid)
    .addNode('blockUserFromAlteringSchema', blockUserFromAlteringSchema)
    .addNode('checkAlterTableSchema', checkAlterTableSchema)
    .addNode('blockTables', blockTables)
    .addNode('evaluateSufficientInfo', evaluateSufficientInfo)
    .addNode('runQueryToDb', runQueryToDb)
    .addNode('generateErrorMessage', generateErrorMessage)
    .addNode('injectionPrevention', injectionPrevention)
    .addNode('checkUserQueryOwnData', checkUserQueryOwnData);

graph.addEdge(START, 'generateSqlQuery');
graph.addEdge('generateSqlQuery', 'determineUserQueryIsValid');

graph.addConditionalEdges('determineUserQueryIsValid', decideToReject, {
    'accept': 'blockTables',
    'reject': 'generateErrorMessage'
});

graph.addConditionalEdges('blockTables', decideToReject, {
    'accept': 'checkAlterTableSchema',
    'reject': 'generateErrorMessage'
});

graph.addConditionalEdges('checkAlterTableSchema', decideAlterSchema, {
    'alter': 'blockUserFromAlteringSchema',
    'not alter': 'evaluateSufficientInfo'
})

graph.addConditionalEdges('blockUserFromAlteringSchema', decideToReject, {
    'accept': 'runQueryToDb',
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
    checkpointer,
})

export default appGraph;