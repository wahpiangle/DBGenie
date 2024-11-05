import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../llm";
import { StringOutputParser } from "@langchain/core/output_parsers";

const INJECTION_PREVENTION_TEMPLATE = `
"Analyze the following SQL statement and determine if it is malicious: Check for signs of SQL injection attacks, such as unexpected input, use of operators like --, ;, or ', attempts to bypass authentication, or manipulate database structures. Additionally, ensure the query adheres to safe coding practices and does not attempt unauthorized actions like data deletion, modification, or access to sensitive information.
Just say 'yes' if the SQL statement is malicious, or 'no' if it is not."

SQL statement: {sql_statement}
`

const injectionPreventionPrompt = ChatPromptTemplate.fromTemplate(
    INJECTION_PREVENTION_TEMPLATE
);

const injectionPreventionChecker = injectionPreventionPrompt.pipe(llm).pipe(new StringOutputParser());

console.log(await injectionPreventionChecker.invoke({
    sql_statement: "SELECT * FROM users WHERE username = 'admin' AND password = 'password' OR '1'='1';"
}))

export { injectionPreventionChecker }