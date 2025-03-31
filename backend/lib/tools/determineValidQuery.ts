import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { StringOutputParser } from "@langchain/core/output_parsers";

const DETERMINE_VALID_QUERY_TEMPLATE = `
Your job is to check if the user's query is regarding database operations before sending it to a text-to-SQL model. You will receive a database schema and a user query.
Based on the user's query, determine if the user intends to interact with the database, queries such as "I want to keep track of payments" or "I want to create a new table" should be considered as database operations.

Follow these rules:
    Ignore irrelevant queries such as general knowledge or personal conversations. If a query is unrelated to database operations, respond with: "I can only assist with database-related queries. Please ask about SQL queries or schema modifications."
    Allow schema modifications: Users may request to create, modify, or delete database tables, columns, and constraints. Handle requests related to CREATE TABLE, ALTER TABLE, DROP TABLE, and similar SQL statements.

The database schema: {database_schema}
The user query: {user_query}
The SQL statement: {sql_statement}
The user ID: {user_id}

If the query is valid and relevant respond with 'Yes' (and nothing else).
If the query is invalid, respond with reasons for why it is invalid.
`

const determineValidQueryPrompt = ChatPromptTemplate.fromTemplate(
    DETERMINE_VALID_QUERY_TEMPLATE
);

const determineValidQuery = determineValidQueryPrompt.pipe(llm).pipe(new StringOutputParser());

export { determineValidQuery }