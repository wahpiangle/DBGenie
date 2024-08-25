### Nodes

from langchain_core.output_parsers import  StrOutputParser
from langchain_core.prompts.prompt import PromptTemplate
from app.config.database import get_db
from app.lib.llm import llm

# from app.utils.rag.retrieval_grader import retrieval_grader
from app.services.message_service import MessageService
from app.utils.rag.agents.retrieval_grader import retrieval_grader
from app.utils.rag.agents.determine_follow_up import determine_follow_up
from app.utils.rag.agents.question_rewriter import question_rewriter
from app.utils.rag.agents.hallucination_grader import hallucination_grader
from app.utils.rag.agents.answer_grader import answer_grader
from app.utils.rag.agents.question_router import question_router

from app.vector_store import vectorstore
from app.utils.searxng import searchSearxng
from app.utils.firecrawl import crawl_and_save_docs

prompt = PromptTemplate(
    template="""
    You are an assistant for question-answering tasks.
    Do not try to guess the answer and provide only the information that you can confidently retrieve.
    Use the following pieces of retrieved context to answer the question.
    If the context doesn't answer the question or is insufficient, just say that you don't know.
    Use three sentences maximum and keep the answer concise.\n
    Question: {question} \n
    Context: {context} \n
    Answer:
    """,
    input_variables=["question", "context"],
)

rag_chain = prompt | llm | StrOutputParser()

def retrieve(state):
    print("---RETRIEVE---")
    question = state["question"]

    # Retrieval
    documents = vectorstore.as_retriever().invoke(question)

    return {"documents": documents, "question": question}


def generate(state):
    print("---GENERATE---")
    question = state["question"]
    documents = state["documents"]

    # RAG generation
    generation = rag_chain.invoke({"context": documents, "question": question})
    return {"documents": documents, "question": question, "generation": generation}


def grade_documents(state):
    print("---CHECK DOCUMENT RELEVANCE TO QUESTION---")
    question = state["question"]
    documents = state["documents"]

    # Score each doc
    filtered_docs = []
    for d in documents:
        score = retrieval_grader.invoke(
            {"question": question, "document": d.page_content}
        )
        grade = score["score"]
        if grade == "yes":
            print("---GRADE: DOCUMENT RELEVANT---")
            filtered_docs.append(d)
        else:
            print("---GRADE: DOCUMENT NOT RELEVANT---")
            continue
    return {"documents": filtered_docs, "question": question}


def transform_query(state):
    """
    Transform the query to produce a better question.

    Args:
        state (dict): The current graph state

    Returns:
        state (dict): Updates question key with a re-phrased question
    """

    print("---TRANSFORM QUERY---")
    question = state["question"]
    documents = state["documents"]

    # Re-write question
    better_question = question_rewriter.invoke({"question": question})
    print("---TRANSFORMED QUESTION---")
    print(better_question)
    return {"documents": documents, "question": better_question}


def web_search(state):
    print("---WEB SEARCH---")
    question = state["question"]

    # Web search
    searchResults = searchSearxng(question)
    for result in searchResults:
        crawl_and_save_docs(result.url)
        
    web_results = vectorstore.as_retriever().invoke(question)
    return {"documents": web_results, "question": question}

def check_follow_up(state):
    print("---DETERMINE FOLLOW UP---")
    chat_id = state["chat_id"]
    chat_history = [msg.content for msg in MessageService(next(get_db())).get_messages_by_chat_id(chat_id, 0, 10)]
    question = state["question"]

    updated_question = determine_follow_up.invoke(
        {
            "chat_history": chat_history,
            "question": question,
        }
    )
    return {"question": updated_question}

### Edges ###

def route_question(state):
    print("---ROUTE QUESTION---")
    question = state["question"]
    retrieved_documents = state["documents"]

    vector_search = question_router.invoke(
        {"question": question, "documents": retrieved_documents}
    )
    if vector_search.strip().lower() == "yes":
        print("---ROUTE QUESTION TO RAG---")
        return "vectorstore"
    elif vector_search.strip().lower() == "no":
        print("---ROUTE QUESTION TO WEB SEARCH---")
        return "web_search"


def decide_to_generate(state):
    print("---ASSESS GRADED DOCUMENTS---")
    state["question"]
    filtered_documents = state["documents"]

    if not filtered_documents:
        # All documents have been filtered check_relevance
        # We will re-generate a new query
        print(
            "---DECISION: ALL DOCUMENTS ARE NOT RELEVANT TO QUESTION, TRANSFORM QUERY---"
        )
        return "transform_query"
    else:
        # We have relevant documents, so generate answer
        print("---DECISION: GENERATE---")
        return "generate"


def grade_generation_v_documents_and_question(state):
    print("---CHECK HALLUCINATIONS---")
    question = state["question"]
    documents = state["documents"]
    generation = state["generation"]

    score = hallucination_grader.invoke(
        {"documents": documents, "generation": generation}
    )
    grade = score["score"]

    # Check hallucination
    if grade == "yes":
        print("---DECISION: GENERATION IS GROUNDED IN DOCUMENTS---")
        # Check question-answering
        print("---GRADE GENERATION vs QUESTION---")
        score = answer_grader.invoke({"question": question, "generation": generation})
        grade = score["score"]
        if grade == "yes":
            print("---DECISION: GENERATION ADDRESSES QUESTION---")
            return "useful"
        else:
            print("---DECISION: GENERATION DOES NOT ADDRESS QUESTION---")
            return "not useful"
    else:
        print("---DECISION: GENERATION IS NOT GROUNDED IN DOCUMENTS, RE-TRY---")
        return "not supported"
