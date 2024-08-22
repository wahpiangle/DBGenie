from langchain.prompts import PromptTemplate
from app.lib.llm import llm
# fetch the last 10 messages from the chat history

prompt = PromptTemplate(
    template="""You are an assistant for question-answering tasks. Use the following pieces of retrieved context to answer the question considering the history of the conversation. 
    If the context provides information to answer the question, respond with a concise answer in three sentences maximum using that information.
    If the context does not provide information, respond with "No Information".
    
    Chat history: {chat_history}

    Question: {question} 

    Answer:
    """,
    
)