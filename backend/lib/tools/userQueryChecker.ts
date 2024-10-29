import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../llm";
import { StringOutputParser } from "@langchain/core/output_parsers";

const USER_QUERY_CHECKER_TEMPLATE = `
Given an SQL statement, determine if it affects the 'users' table. Return only 'yes' or 'no'. Don't worry about the syntax of the SQL statement, just focus on the table name.

SQL statement: {sql_statement}
`

const userQueryCheckerPrompt = ChatPromptTemplate.fromTemplate(
    USER_QUERY_CHECKER_TEMPLATE
);

const userQueryChecker = userQueryCheckerPrompt.pipe(llm).pipe(new StringOutputParser());

// console.log(await userQueryChecker.invoke({
//     sql_statement: "INSERT INTO users where id = 1"
// }));

export { userQueryChecker }