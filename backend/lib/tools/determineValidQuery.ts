import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { StringOutputParser } from "@langchain/core/output_parsers";

const DETERMINE_VALID_QUERY_TEMPLATE = `
Your job is to check if the user's query is regarding database operations before sending it to a text-to-SQL model. You will receive a database schema, a user query, and the user's recent conversation history.

Your Task:
Check if the user's current query, possibly in context with the conversation history, indicates an intent to:
    - Query the database (e.g., SELECT statements)
    - Modifying the database schema (deleting tables, updating tables, creating tables)
    - Insert, update, or delete data (e.g., INSERT INTO, DELETE FROM)
- If the user's current query depends on context (e.g., a continuation like “bookings” after “Show all data”), use the most recent relevant query in the history to resolve the meaning.

Response Rules:
- If the query (with context) is a valid database operation, respond with: Yes
- If it is unclear (e.g., missing table or column names, vague terms like "data"), respond with a clarification such as:
    - “What table do you wish to create?”
    - “What column do you want to add and to which table?”
    - “What data do you want to show?”
- If the query is not database-related, respond with: "I can only assist with database-related queries. Please ask about SQL queries or schema modifications."

Input format:
The database schema: {database_schema}
The user query: {user_query}
The SQL statement: {sql_statement}
The user ID: {user_id}
The conversation history: {conversation_history}
`

const determineValidQueryPrompt = ChatPromptTemplate.fromTemplate(
    DETERMINE_VALID_QUERY_TEMPLATE
);

const determineValidQuery = determineValidQueryPrompt.pipe(llm).pipe(new StringOutputParser());

export { determineValidQuery }