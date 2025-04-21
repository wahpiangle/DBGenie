import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { JsonOutputParser, StringOutputParser } from "@langchain/core/output_parsers";

const OWN_DATA_CHECKER_TEMPLATE = `
You are tasked with validating and executing an SQL statement provided by a user.The SQL statement must comply with the following rules:

Scope Restriction:
- The statement can only modify or affect relationships to other tables related to the specified user.
- If the statement is to retrieve data, it must only return data related to the user or their relationships.

You are provided:
- SQL Statement: {sql_statement}
- User JSON: {user_json}, This JSON contains information about the user, including their user_id and their relationships to other tables.
- User ID: {user_id}

Validation Rules:
- Parse the JSON to extract the user_id and relationship details.
- Ensure the SQL statement targets only rows in related tables corresponding to the user_id or its relationships. Any action outside these bounds must be flagged.


Validation Output:
- If the statement is valid, return in JSON format
{{
    "valid": true,
    "errorMessage": null,
}}
- If the statement is invalid, return an error message indicating the issue in this format:
{{
    "valid": false,
    "errorMessage": ...,
}}

`

const ownDataCheckerPrompt = ChatPromptTemplate.fromTemplate(
    OWN_DATA_CHECKER_TEMPLATE
);

const ownDataChecker = ownDataCheckerPrompt.pipe(llm).pipe(new JsonOutputParser());

// console.log(await ownDataChecker.invoke({
//   sql_statement: "INSERT INTO Booking (id, status) VALUES ('cm2hejqr000009o2m2snq4l86', 'pending'); UPDATE Property SET status = 'confirmed' WHERE id = 'cm2hejqr000009o2m2snq4l86';",
//   user_json: JSON.stringify(ids),
// }))

export { ownDataChecker }