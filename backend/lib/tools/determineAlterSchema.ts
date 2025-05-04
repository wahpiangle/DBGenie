import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { StringOutputParser } from "@langchain/core/output_parsers";

const DETERMINE_ALTER_SCHEMA_TEMPLATE = `
Given an SQL statement, determine if the user is trying to alter the table schema (including creating and removing tables)
Don't worry about the syntax of the SQL statement, just focus on the type of statement.

SQL statement: {sql_statement}

Respond in plain text with 'yes' or 'no'.
`

const determineAlterSchemaPrompt = ChatPromptTemplate.fromTemplate(
    DETERMINE_ALTER_SCHEMA_TEMPLATE
);

const determineAlterSchema = determineAlterSchemaPrompt.pipe(llm).pipe(new StringOutputParser());

export { determineAlterSchema }