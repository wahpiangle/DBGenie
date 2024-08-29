from langgraph.graph import END, StateGraph, START
from typing import List
from typing_extensions import TypedDict
from app.utils.rag.tool_nodes import (
    generate_general_response,
    retrieve,
    generate,
    grade_documents,
    route_question,
    transform_query,
    web_search,
    decide_to_generate,
    grade_generation_v_documents_and_question,
    check_general_question
)
class GraphState(TypedDict):
    """
    Represents the state of our graph.

    Attributes:
        question: question
        chat_id: chat_id
        generation: LLM generation
        documents: list of documents
    """
    chat_id: int
    question: str
    generation: str
    documents: List[str]

workflow = StateGraph(GraphState)

workflow.add_node("web_search", web_search)
workflow.add_node("retrieve", retrieve)  
workflow.add_node("initial_retrieve", retrieve)  
workflow.add_node("grade_documents", grade_documents)
workflow.add_node("generate", generate)  
workflow.add_node("transform_query", transform_query)  
workflow.add_node("generate_general_response", generate_general_response)

# Build graph
workflow.add_conditional_edges(
    START,
    check_general_question,
    {
        "yes": "initial_retrieve",
        "no": "generate_general_response",
    }
)
workflow.add_edge("generate_general_response", END)

workflow.add_conditional_edges(
    "initial_retrieve",
    route_question,
    {
        "web_search": "web_search",
        "vectorstore": "retrieve",
    },
)
workflow.add_edge("web_search", "generate")
workflow.add_edge("retrieve", "grade_documents")
workflow.add_conditional_edges(
    "grade_documents",
    decide_to_generate,
    {
        "transform_query": "transform_query",
        "generate": "generate",
    },
)
workflow.add_conditional_edges(
    "generate",
    grade_generation_v_documents_and_question,
    {
        "not supported": "generate",
        "useful": END,
        "not useful": "transform_query",
    },
)
workflow.add_edge("transform_query", "initial_retrieve")

# Compile
app = workflow.compile()