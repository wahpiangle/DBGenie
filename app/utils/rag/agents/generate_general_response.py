from app.lib.llm import llm
from langchain_core.output_parsers import  StrOutputParser
from langchain.prompts import PromptTemplate

prompt = PromptTemplate(
    template="""You are a chatbot designed to generate a general response to a user's input.
    
    This is the user input: {question}
    
    Provide a general response to the user input.
    Keep your response concise and to the point.
    """,
    input_variables=["question"],
)
general_response_generator = prompt | llm | StrOutputParser()