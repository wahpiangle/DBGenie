import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { StringOutputParser } from "@langchain/core/output_parsers";

const DETERMINE_VALID_QUERY_TEMPLATE = `
Your job is to check if the user's query is regarding database operations before sending it to a text-to-SQL model. You will receive a database schema, a user query, and the user's recent conversation history.

Based on the user's query and conversation history, determine if the user intends to interact with the database. Queries such as "I want to keep track of payments" or "I want to create a new table with columns for name, amount, and date" should be considered as database operations.

Follow these rules:

Use the conversation history to understand the context of the current query. This will help identify if an ambiguous query is database-related or not.

Ignore irrelevant queries such as general knowledge or personal conversations. If a query is unrelated to database operations, respond with:
"I can only assist with database-related queries. Please ask about SQL queries or schema modifications."

Allow schema modifications: Users may request to create, modify, or delete database tables, columns, and constraints. Handle requests related to CREATE TABLE, ALTER TABLE, DROP TABLE, and similar SQL statements.

If the user makes a vague request (e.g., "I want to create a table", "Add a column", "Change the schema") without specifying the table name, column names, or structure, respond with:
"What table do you wish to create?",
"What column do you want to add and to which table?",
or another relevant clarification question depending on the intent.

Input format:
The database schema: {database_schema}
The user query: {user_query}
The SQL statement: {sql_statement}
The user ID: {user_id}
The conversation history: {conversation_history}

If the query is valid and relevant, respond with 'Yes' (and nothing else).
If the query is invalid due to lack of clarity, respond with a clarification question.
If the query is completely unrelated to database operations, respond with: "I can only assist with database-related queries. Please ask about SQL queries or schema modifications."
`

const determineValidQueryPrompt = ChatPromptTemplate.fromTemplate(
    DETERMINE_VALID_QUERY_TEMPLATE
);

const determineValidQuery = determineValidQueryPrompt.pipe(llm).pipe(new StringOutputParser());

export { determineValidQuery }