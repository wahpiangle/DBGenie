import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../llm";
import { JsonOutputParser } from "@langchain/core/output_parsers";
import { db } from "../chatbot";
import { tableColumnGenerator } from "./tableColumnGenerator";

interface QuestionEvaluatorOutput {
    evaluation: string;
    feedback: string;
}

const QUESETION_EVALUATION_TEMPLATE = `
You are a database assistant. Your task is to evaluate whether a given user query contains sufficient information to execute an insert or update operation on a database table.

You will receive the following inputs:

    Table Information: Details about the table, including its name, columns, and constraints (e.g., primary keys, not-null columns, foreign key relationships).
    User Query: The user's natural language input describing the intended operation.
    SQL Statement: A partially or fully constructed SQL statement based on the user query.

Your evaluation must determine:
    If all required fields for the table's schema (e.g., not-null fields) are provided in the user query or SQL statement.
    For INSERT operations, the primary key field is optional unless explicitly provided by the user.
    Whether the user's intent for the operation (insert or update) is clear and matches the provided SQL statement.
    If any critical details are missing, specify what is missing and provide clear feedback to help complete the operation.

Respond in the following format in JSON:
    "evaluation": "Sufficient" or "Insufficient"
    "feedback": "Feedback message here"

Input: {input}
Table: {table}
SQL Statement: {sql_statement}

Just return plain json, don't do it in markdown format.

`

const questionEvaluationPrompt = ChatPromptTemplate.fromTemplate(
    QUESETION_EVALUATION_TEMPLATE
);

const questionEvaluation = questionEvaluationPrompt.pipe(llm).pipe(new JsonOutputParser());
// const response = await questionEvaluation.invoke({
//     input: "Insert a new employee record of John Doe of age 30",
//     table: tableColumnGenerator(db),
//     sql_statement: "INSERT INTO Employee (name, age, department_id) VALUES ('John Doe', 30, 6)"
// }) as QuestionEvaluatorOutput

export { questionEvaluation };
export type { QuestionEvaluatorOutput };