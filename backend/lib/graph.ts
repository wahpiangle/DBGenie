import { END, MemorySaver, START, StateGraph } from "@langchain/langgraph";
import { blockTables, checkUserQuery, decideToReject, evaluateSufficientInfo, generateErrorMessage, generateReadQueryResult, generateSqlQuery, GraphState, injectionPrevention, runQueryToDb } from "./nodes";

const graph = new StateGraph(GraphState)
    .addNode('generateSqlQuery', generateSqlQuery)
    // .addNode('checkUserQuery', checkUserQuery)
    .addNode('blockTables', blockTables)
    // .addNode('evaluateSufficientInfo', evaluateSufficientInfo)
    // .addNode('runQueryToDb', runQueryToDb)
    .addNode('generateErrorMessage', generateErrorMessage)
// .addNode('injectionPrevention', injectionPrevention)
// .addNode('generateReadQueryResult', generateReadQueryResult)

graph.addEdge(START, 'generateSqlQuery')
graph.addEdge('generateSqlQuery', 'blockTables');
graph.addConditionalEdges('blockTables', decideToReject, {
    'accept': END,
    'reject': 'generateErrorMessage'
});
graph.addEdge('blockTables', END);
// graph.addEdge('generateSqlQuery', 'blockTables')
// graph.addConditionalEdges('blockTables', decideToReject);

// graph.addEdge('generateErrorMessage', END)
const app = graph.compile({
    checkpointer: new MemorySaver(),
})

app.invoke({
    question: "How many users are there?",
},
    {
        configurable: {
            thread_id: '1234'
        }
    },
)