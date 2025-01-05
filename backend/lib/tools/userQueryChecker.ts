import { ChatPromptTemplate } from "@langchain/core/prompts";
import { StringOutputParser } from "@langchain/core/output_parsers";
import { llm } from "../chatbot";

const USER_QUERY_CHECKER_TEMPLATE = `
Given an SQL statement, determine if it affects the {blockedTables} tables. Return only 'yes' or 'no'. Don't worry about the syntax of the SQL statement, just focus on the table names.

SQL statement: {sql_statement}
`

const userQueryCheckerPrompt = ChatPromptTemplate.fromTemplate(
    USER_QUERY_CHECKER_TEMPLATE
);

const userQueryChecker = userQueryCheckerPrompt.pipe(llm).pipe(new StringOutputParser());

export { userQueryChecker }