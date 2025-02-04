import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { JsonOutputParser } from "@langchain/core/output_parsers";

interface QuestionEvaluatorOutput {
    evaluation: string;
    feedback: string;
}

const QUESETION_EVALUATION_TEMPLATE = `
You are a database assistant. Your task is to evaluate whether a given user query contains sufficient information to execute an insert or update operation on a database table and respond in JSON.

You will receive the following inputs:

    Table Information: Details about the table, including its name, columns, and constraints (e.g., primary keys, non-null columns, foreign key relationships).
    User Query: The user's natural language input describing the intended operation.
    SQL Statement: A partially or fully constructed SQL statement based on the user query.

Your evaluation must determine:
    If all non-null fields for the table's schema are provided in the user query or SQL statement.
    Whether the user's intent for the operation (insert or update) is clear and matches the provided SQL statement.
    If any critical details are missing, specify what is missing and provide clear feedback to help complete the operation.
    The placeholder "<value>" used to represent missing values in the user query or SQL statement should not be considered as a valid input.

    Input: {input}
    Table: {table}
    SQL Statement: {sql_statement}

    Respond in the following format in JSON:
        "evaluation": "Sufficient" or "Insufficient"
        "feedback": "Feedback message here"

`

const questionEvaluationPrompt = ChatPromptTemplate.fromTemplate(
    QUESETION_EVALUATION_TEMPLATE
);

const questionEvaluation = questionEvaluationPrompt.pipe(llm).pipe(new JsonOutputParser());

export { questionEvaluation };
export type { QuestionEvaluatorOutput };