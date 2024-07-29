import "cheerio";
import { CheerioWebBaseLoader } from "@langchain/community/document_loaders/web/cheerio";
import { RecursiveCharacterTextSplitter } from "langchain/text_splitter";
import { pull } from "langchain/hub";
import { ChatPromptTemplate } from "@langchain/core/prompts";
import { StringOutputParser } from "@langchain/core/output_parsers";
import { createStuffDocumentsChain } from "langchain/chains/combine_documents";
import { pgvectorStore } from "./embeddings";
import { Ollama } from "@langchain/community/llms/ollama";

const ollama = new Ollama({
    baseUrl: "https://localhost:11434",
    model: "llama3.1:8b",
});

const loader = new CheerioWebBaseLoader(
    "https://lilianweng.github.io/posts/2023-06-23-agent/"
);

const docs = await loader.load();

const textSplitter = new RecursiveCharacterTextSplitter({
    chunkSize: 1000,
    chunkOverlap: 200,
});
const splits = await textSplitter.splitDocuments(docs);
const vectorStore = await pgvectorStore.addDocuments(splits);
const retriever = pgvectorStore.asRetriever();

// Retrieve and generate using the relevant snippets of the blog.
const prompt = await pull<ChatPromptTemplate>("rlm/rag-prompt");

const ragChain = await createStuffDocumentsChain({
    ollama,
    prompt,
    outputParser: new StringOutputParser(),
});

const retrievedDocs = await retriever.invoke("what is task decomposition");