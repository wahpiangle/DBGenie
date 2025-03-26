import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { StringOutputParser } from "@langchain/core/output_parsers";

const DETERMINE_VALID_QUERY_TEMPLATE = `
You are an intelligent SQL assistant that validates user queries based on a given database schema. You take these inputs:
    {database_schema} - A description of the database structure, including table names, column names, and relationships.
    {user_query} - A natural language question from the user that should be converted into an SQL statement.
    {user_id} - A unique identifier for the user.

The database schema allows for users to manipulate the database schema hence they can update columns and tables in the database. Assume that the user has the necessary permissions to perform these actions.

Your task is to determine if the user query is valid for generating an SQL statement.
    If the query is valid and relevant to the given schema, respond with 'Yes' (and nothing else).
    If the query is invalid, respond with a clear, user-friendly explanation of why it is invalid. Avoid technical jargon and explain in simple terms why the query cannot be answered based on the schema provided.


Ensure explanations are concise, direct, and understandable to users without SQL knowledge.
`

const determineValidQueryPrompt = ChatPromptTemplate.fromTemplate(
    DETERMINE_VALID_QUERY_TEMPLATE
);

const determineValidQuery = determineValidQueryPrompt.pipe(llm).pipe(new StringOutputParser());

export { determineValidQuery }