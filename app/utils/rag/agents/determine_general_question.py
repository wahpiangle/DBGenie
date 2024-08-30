from langchain.prompts import PromptTemplate
from app.lib.llm import llm
from langchain_core.output_parsers import JsonOutputParser

prompt = PromptTemplate(
    template="""You are an intelligent chatbot designed to assist users by answering questions.
    Determine the complexity of the user's query to decide whether a web search or document retrieval is required.
    A web search or document retrieval is required if the question is not answerable with the information you have.

    Query: {question} 

    Respond with a binary "yes" or "no" in JSON format to indicate whether the user's query is complex and requires a web search or document retrieval.
    Provide the binary score as a JSON with a single key 'score' and no preamble or explanation.
    """,
    
)

determine_general_question = prompt | llm | JsonOutputParser()