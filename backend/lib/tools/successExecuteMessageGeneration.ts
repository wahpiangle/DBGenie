import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { StringOutputParser } from "@langchain/core/output_parsers";

const SUCCESS_EXECUTE_MESSAGE_GENERATION_TEMPLATE = `
Given an SQL statement, generate a message that can be used to inform the user that the statement was successfully executed.

SQL statement: {sql_statement}
Query Result: {result}

Respond with the message that should be displayed to the user.
`

const successExecuteMessageGenerationPrompt = ChatPromptTemplate.fromTemplate(
    SUCCESS_EXECUTE_MESSAGE_GENERATION_TEMPLATE
);

const successExecuteMessageGeneration = successExecuteMessageGenerationPrompt.pipe(llm).pipe(new StringOutputParser());

export default successExecuteMessageGeneration;