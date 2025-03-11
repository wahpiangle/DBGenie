import { END, START, StateGraph } from "@langchain/langgraph";
import { blockTables, checkAlterTableSchema, checkUserQueryOwnData, decideAlterSchema, decideToReject, determineUserQueryIsValid, evaluateSufficientInfo, generateErrorMessage, generateSqlQuery, GraphState, injectionPrevention, runQueryToDb } from "./nodes";

const graph = new StateGraph(GraphState)
    .addNode('determineUserQueryIsValid', determineUserQueryIsValid)
    .addNode('generateSqlQuery', generateSqlQuery)
    .addNode('checkAlterTableSchema', checkAlterTableSchema)
    .addNode('blockTables', blockTables)
    .addNode('evaluateSufficientInfo', evaluateSufficientInfo)
    .addNode('runQueryToDb', runQueryToDb)
    .addNode('generateErrorMessage', generateErrorMessage)
    .addNode('injectionPrevention', injectionPrevention)
    .addNode('checkUserQueryOwnData', checkUserQueryOwnData);

graph.addEdge(START, 'determineUserQueryIsValid');

graph.addConditionalEdges('determineUserQueryIsValid', decideToReject, {
    'accept': 'generateSqlQuery',
    'reject': 'generateErrorMessage'
});

graph.addEdge('generateSqlQuery', 'blockTables');

graph.addConditionalEdges('blockTables', decideToReject, {
    'accept': 'checkAlterTableSchema',
    'reject': 'generateErrorMessage'
});

graph.addConditionalEdges('checkAlterTableSchema', decideAlterSchema, {
    'alter': 'runQueryToDb',
    'not alter': 'evaluateSufficientInfo'
})

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