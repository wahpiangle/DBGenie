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

// await appGraph.invoke({
//     question: "Create a property of name 'aaa",
//     user: {
//         id: "cm3pb9uml0000ki5wq8pb8mb6",
//         name: "test",
//         email: "t@t.com",
//         password: "",
//         role: "MANAGER",
//         createdAt: "2024-11-20T03:15:46.885Z",
//         updatedAt: "2024-11-20T03:19:40.793Z",
//         verified: true,
//     }
// });

export default appGraph;