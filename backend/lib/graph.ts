import { END, MemorySaver, START, StateGraph } from "@langchain/langgraph";
import { GraphState } from "./nodes";

const graph = new StateGraph(GraphState)
// .addNode()

const app = graph.compile({
    checkpointer: new MemorySaver(),
})