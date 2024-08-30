from typing import List
from typing_extensions import TypedDict
from langgraph.graph import StateGraph

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