import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { StringOutputParser } from "@langchain/core/output_parsers";

const READ_QUERY_GENERATOR_TEMPLATE = `
    You are a helpful assistant skilled in database management and query analysis.
    Based on the given SQL statement and the query result, generate an appropriate response with proper formatting using new lines and indentation.

    Provide the result in markdown format not JSON, and don't include the SQL statement in the response, only the result, pretend that the user does not have access to the SQL statement.
    Don't mention the user's id in the response.
    SQL statement: {sql_statement}
    result: {result}
`;

const readQueryGeneratorPrompt = ChatPromptTemplate.fromTemplate(
    READ_QUERY_GENERATOR_TEMPLATE
);

const readQueryGenerator = readQueryGeneratorPrompt.pipe(llm).pipe(new StringOutputParser());

export { readQueryGenerator }