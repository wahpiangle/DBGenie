from langchain.prompts import PromptTemplate
from langchain_core.output_parsers import StrOutputParser
from app.lib.llm import llm

# Prompt
prompt = PromptTemplate(
    template=""""You are a chatbot designed to determine whether a user's input is a simple question,
    a simple statement, or a complex query that requires more information.
    Follow these instructions:
    
    Simple Question: If the input is a basic or trivial question (e.g., arithmetic like 'What is 1+1?', factual like 'How many grams in a kilogram?', or general inquiries like 'What's the time?'), provide a direct answer.
    Simple Statement: If the input is a simple statement or greeting (e.g., 'Hi', 'Hello', 'How are you?'), respond appropriately to acknowledge the statement or greeting.
    Complex Input: If the input is anything else that seems more complex or requires additional context (e.g., follow-up questions, multi-part questions, or queries involving specific knowledge), return the response: 'More information needed'.
    
    Your response should only fit into one of these categories.
    If uncertain, treat the input as complex and respond with 'More information needed'.
    
    This is the user input: {question}
    """,
    input_variables=["question"],
)

general_question_identifier = prompt | llm | StrOutputParser()
