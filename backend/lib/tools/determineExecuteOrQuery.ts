import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { StringOutputParser } from "@langchain/core/output_parsers";

const DETERMINE_EXECUTE_OR_QUERY_TEMPLATE = `
Given an SQL statement, determine if it is a SELECT statement or not. Don't worry about the syntax of the SQL statement, just focus on the type of statement.

SQL statement: {sql_statement}

Respond with 'yes' if the statement is a SELECT statement, and 'no' if it is not.
`

const determineExecuteOrQueryPrompt = ChatPromptTemplate.fromTemplate(
    DETERMINE_EXECUTE_OR_QUERY_TEMPLATE
);

const determineExecuteOrQuery = determineExecuteOrQueryPrompt.pipe(llm).pipe(new StringOutputParser());

// console.log(await determineExecuteOrQuery.invoke({
//     sql_statement: "INSERT INTO employees (id, name, role) VALUES (?, ?, ?);"
// }))

export { determineExecuteOrQuery }