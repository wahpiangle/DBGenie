### Nodes

from langchain.schema import Document
from langchain_core.output_parsers import JsonOutputParser, StrOutputParser
from langchain_core.prompts.prompt import PromptTemplate
from app.lib.llm import llm

# from app.utils.rag.retrieval_grader import retrieval_grader
from app.utils.rag.question_rewriter import question_rewriter
from app.utils.rag.hallucination_grader import hallucination_grader
from app.utils.rag.answer_grader import answer_grader
from app.utils.rag.question_router import question_router

from app.vector_store import vectorstore
from app.utils.searxng import searchSearxng
from app.utils.firecrawl import crawl_and_save_docs

prompt = PromptTemplate(
    template="""
    You are an assistant for question-answering tasks. Use the following pieces of retrieved context to answer the question. If you don't know the answer, just say that you don't know. Use three sentences maximum and keep the answer concise.\n
    Question: {question} \n
    Context: {context} \n
    Answer:
    """,
    input_variables=["question", "context"],
)

rag_chain = prompt | llm | StrOutputParser()


prompt = PromptTemplate(
    template="""You are a grader assessing relevance of a retrieved document to a user question. \n 
    Here is the retrieved document: \n\n {document} \n\n
    Here is the user question: {question} \n
    If the document contains keywords related to the user question, grade it as relevant. \n
    It does not need to be a stringent test. The goal is to filter out erroneous retrievals. \n
    Give a binary score 'yes' or 'no' score to indicate whether the document is relevant to the question. \n
    Provide the binary score as a JSON with a single key 'score' and no premable or explanation.""",
    input_variables=["question", "document"],
)

retrieval_grader = prompt | llm | JsonOutputParser()


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
    """
    Determines whether the retrieved documents are relevant to the question.

    Args:
        state (dict): The current graph state

    Returns:
        state (dict): Updates documents key with only filtered relevant documents
    """

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
