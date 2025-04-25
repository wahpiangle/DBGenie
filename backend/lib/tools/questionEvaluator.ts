import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { JsonOutputParser } from "@langchain/core/output_parsers";

interface QuestionEvaluatorOutput {
  evaluation: string;
  feedback: string;
}

const QUESETION_EVALUATION_TEMPLATE = `
You are a database assistant. Your task is to evaluate whether a given user query contains sufficient information to execute an insert or update operation on a database table and respond in JSON.
If the user query is just to read data, you should return "Sufficient" as the evaluation.

You will receive the following inputs:
- Table Information: Details about the table, including its name, columns, and constraints (e.g., primary keys, non-null columns, foreign key relationships).
- SQL Statement: A partially or fully constructed SQL statement based on the user query.
- Auto-Supplied Columns (optional): A list of column names that are automatically supplied by the system (e.g., user_id, created_at, updated_at).

The inputs are as follows:
  - Table Information : {table},
  - SQL Statement: {sql_statement},
  - Auto-Supplied Columns: {auto_supplied_columns}

Your evaluation must determine:
- If all non-null fields for the table's schema are either:
  - Provided directly in the user query or SQL statement,
  - Resolved via valid subqueries or lookups (e.g., SELECT id FROM properties WHERE name = 'XYZ'), or
  - Listed in the auto-supplied columns.

- Insert operations do not require the primary key (e.g., id) to be provided explicitly.
- Placeholders such as <value> are not valid inputs and count as missing information.

If any critical information is missing, mark the evaluation as "Insufficient" and provide a specific feedback message indicating what is required to complete the operation.

Respond in the following format in JSON:
{{
    "evaluation": "Sufficient" or "Insufficient",
    "feedback": "Feedback message here"
}}

`

const questionEvaluationPrompt = ChatPromptTemplate.fromTemplate(
  QUESETION_EVALUATION_TEMPLATE
);

const questionEvaluation = questionEvaluationPrompt.pipe(llm).pipe(new JsonOutputParser());

export { questionEvaluation };
export type { QuestionEvaluatorOutput };