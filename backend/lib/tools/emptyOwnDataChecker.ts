import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { JsonOutputParser, StringOutputParser } from "@langchain/core/output_parsers";

const EMPTY_OWN_DATA_CHECKER_TEMPLATE = `
You are tasked with validating and executing an SQL statement provided by a user.The SQL statement must comply with the following rules:

Scope Restriction:
- Check if the sql statement is to modify data with user_id that is not the same as the user_id in the JSON. If so, it is invalid.

You are provided:
- SQL Statement: {sql_statement}
- User ID: {user_id}

Validation Output:
- If the SQL statement is valid, return in JSON format the user_id and the list of related tables that will be affected by the statement in this format:
{{
    "valid": true,
    "user_id": 123,
    "tables": ["user_roles", "permissions"]
}}
- If the SQL statement is invalid, return an error message indicating the issue with the key "error" in this format:
{{
    "valid": false,
    "errorMessage": ...
}}
`

const emptyOwnDataCheckerPrompt = ChatPromptTemplate.fromTemplate(
    EMPTY_OWN_DATA_CHECKER_TEMPLATE
);

const emptyOwnDataChecker = emptyOwnDataCheckerPrompt.pipe(llm).pipe(new JsonOutputParser());

export { emptyOwnDataChecker }