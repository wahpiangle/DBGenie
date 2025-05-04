import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { JsonOutputParser } from "@langchain/core/output_parsers";

interface QuestionEvaluatorOutput {
  evaluation: string;
  feedback: string;
}

const QUESETION_EVALUATION_TEMPLATE = `
You are a database assistant.
Your task is to evaluate whether a given user query contains sufficient information to perform an INSERT or UPDATE operation on a database table, based on the table schema.
Your response must be in JSON format.

You will receive the following inputs:
- Table Information: Details about the table, including its name, columns, and constraints (e.g., primary keys, non-null columns, foreign key relationships).
- SQL Statement: An SQL statement derived from the user query.
- Auto-Supplied Columns (optional): A list of column names that are automatically supplied by the system (e.g., user_id, created_at).

The inputs are as follows:
  - Table Information : {table},
  - SQL Statement: {sql_statement},
  - Auto-Supplied Columns: {auto_supplied_columns}

Evaluation Criteria:
- For INSERT operations:
  - All non-null columns (except auto-supplied ones) must have values provided in the SQL statement or derived via subqueries.
  - The primary key does not need to be explicitly provided if it's auto-generated (e.g., SERIAL or UUID).
- For UPDATE operations:
  - It is not required to update all non-null columns.
  - The SQL must include a WHERE clause with a column or condition that identifies the row(s) to be updated.
- For SELECT or other read-only queries, simply return "evaluation": "Sufficient"

Respond in the following format in JSON:
{{
    "evaluation": "Sufficient" or "Insufficient",
    "feedback":  "Specific feedback describing what is missing or why the statement is sufficient."
}}

`

const questionEvaluationPrompt = ChatPromptTemplate.fromTemplate(
  QUESETION_EVALUATION_TEMPLATE
);

const questionEvaluation = questionEvaluationPrompt.pipe(llm).pipe(new JsonOutputParser());

export { questionEvaluation };
export type { QuestionEvaluatorOutput };