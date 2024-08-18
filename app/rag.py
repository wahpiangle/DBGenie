from app.lib import llm
from app.utils.firecrawl import crawl_and_save_docs
from app.vector_store import vectorstore
from langchain.chains.history_aware_retriever import create_history_aware_retriever
from langchain.chains.retrieval import create_retrieval_chain
from langchain_core.prompts import MessagesPlaceholder
from langchain_core.prompts import ChatPromptTemplate
from langchain.chains.combine_documents import create_stuff_documents_chain
from langchain_community.chat_message_histories import ChatMessageHistory
from langchain_core.chat_history import BaseChatMessageHistory
from langchain_core.runnables.history import RunnableWithMessageHistory

retriever = vectorstore.as_retriever()

# chat history prompt
contextualize_q_prompt = ChatPromptTemplate.from_messages(
    [
        (
            "system",
            (
                "Given a chat history and the latest user question "
                "which might reference context in the chat history, "
                "formulate a standalone question which can be understood "
                "without the chat history. Do NOT answer the question, "
                "just reformulate it if needed and otherwise return it as is."
            ),
        ),
        MessagesPlaceholder("chat_history"),
        ("human", "{input}"),
    ]
)

# Retrieve and generate using the relevant snippets of the blog.
system_prompt = (
    "You are an assistant for question-answering tasks. "
    "Use the following pieces of retrieved context to answer "
    "the question. If you don't know the answer, say that you "
    "don't know. Use three sentences maximum and keep the "
    "answer concise."
    "\n\n"
    "{context}"
)

qa_prompt = ChatPromptTemplate.from_messages(
    [
        ("system", system_prompt),
        MessagesPlaceholder("chat_history"),
        ("human", "{input}"),
    ]
)

# add history aware retriever
history_aware_retriever = create_history_aware_retriever(
    llm, retriever, contextualize_q_prompt
)

rag_chain = create_retrieval_chain(
    history_aware_retriever,
    create_stuff_documents_chain(llm, qa_prompt),
)

store = {}


def get_session_history(session_id: str) -> BaseChatMessageHistory:
    if session_id not in store:
        store[session_id] = ChatMessageHistory()
    return store[session_id]


conversational_rag_chain = RunnableWithMessageHistory(
    rag_chain,
    get_session_history,
    input_messages_key="input",
    history_messages_key="chat_history",
    output_messages_key="answer",
)

conversational_rag_chain.invoke(
    {"input": "What does Jina AI do?"},
    config={"configurable": {"session_id": "abc123"}},
)["answer"]

conversational_rag_chain.invoke(
    {"input": "Please tell me more about it?"},
    config={"configurable": {"session_id": "abc123"}},
)["answer"]
