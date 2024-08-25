from langchain.prompts import PromptTemplate
from app.lib.llm import llm
from langchain_core.output_parsers import StrOutputParser

prompt = PromptTemplate(
    template="""You are an intelligent chatbot designed to assist users by answering questions.
    Below is the current user's message and their chat history.
    Your task is to determine if the message is a follow-up to a previous question.
    If it is a follow-up, rephrase the question so that it no longer requires context from the chat history.
    You can only respond with the original question or the revised question, and nothing else. Don't include statements like: "Based on your previous message, I think you're asking about...".
    
    Chat history: {chat_history}

    Question: {question} 

    Response:
    """,
    
)

determine_follow_up = prompt | llm | StrOutputParser()