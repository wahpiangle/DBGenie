import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../llm";
import { StringOutputParser } from "@langchain/core/output_parsers";

const QUESETION_EVALUATION_TEMPLATE = `
Given a user query and a generated SQL statement, determine if the user's query provides enough information to execute the SQL statement accurately. Focus on whether each required field for the statement is specified, particularly for INSERT statements, where missing fields will prevent successful insertion.

If the information is sufficient, respond with 'Query Complete', and if any essential fields or values are missing, specify which ones are missing and request the necessary information from the user.

Examples of missing information may include:

    Field names for INSERT statements,
    Conditions for UPDATE or DELETE statements,
    Any required data needed to meet NOT NULL constraints or primary key requirements in INSERT or UPDATE statements.

For example, given the user input: 'Insert a new employee record,' and a generated SQL query INSERT INTO employees (id, name, role) VALUES (?, ?, ?);, respond with: 'Please provide values for id, name, and role to complete the insert.'

User Query: {input}
SQL statement: {sql_statement}

`

const questionEvaluationPrompt = ChatPromptTemplate.fromTemplate(
    QUESETION_EVALUATION_TEMPLATE
);

const questionEvaluation = questionEvaluationPrompt.pipe(llm).pipe(new StringOutputParser());

export { questionEvaluation }