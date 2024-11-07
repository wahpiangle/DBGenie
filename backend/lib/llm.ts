import { Ollama } from "@langchain/ollama";

export const llm = new Ollama({
    model: "llama3.2:3b",
});
