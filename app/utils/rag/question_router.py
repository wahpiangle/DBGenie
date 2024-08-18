### Router

from langchain.prompts import PromptTemplate
from langchain_core.output_parsers import StrOutputParser
from app.lib.llm import llm

# LLM

prompt = PromptTemplate(
    template="""You are a chatbot.
    You will be given a context and a user query.
    Your task is to determine if the context contains sufficient information to provide an accurate and relevant response to the user's query.

    1. First, carefully analyze the context for details, data, or facts relevant to the query.
    2. If the context has enough information to directly answer the query, return "yes"
    3. If the context does not contain enough information to answer the query or is missing key details, return "no"

    Respond only with "yes" or "no".

    Context: {documents}
    User Query: {question}""",
    input_variables=["question", "documents"],
)

question_router = prompt | llm | StrOutputParser()
