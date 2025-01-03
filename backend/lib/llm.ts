import { ChatOpenAI } from "@langchain/openai";

export const llm = new ChatOpenAI({ model: "gpt-4o-mini-2024-07-18" });