import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { StringOutputParser } from "@langchain/core/output_parsers";

const DETERMINE_VALID_QUERY_TEMPLATE = `
Given the user query and the database schema, determine if the user query is valid and can be translated into a SQL statement.

Take note that:
- The user's id is {user_id}.
Some examples of invalid queries include:
- Queries that reference non-existent tables or columns.

Database Schema: {database_schema}
User Query: {user_query}

If the query is valid, just respond with 'yes' and nothing else.
If the query is invalid, respond with reasons why it is invalid, make sure that the reasons is to the end user assuming that they have no knowledge of SQL. Just give them a general idea of why it is invalid.
`

const determineValidQueryPrompt = ChatPromptTemplate.fromTemplate(
    DETERMINE_VALID_QUERY_TEMPLATE
);

const determineValidQuery = determineValidQueryPrompt.pipe(llm).pipe(new StringOutputParser());

export { determineValidQuery }