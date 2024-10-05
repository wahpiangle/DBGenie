import { Ollama } from "@langchain/ollama";

export const llm = new Ollama({
    model: "phi3:mini"
});
