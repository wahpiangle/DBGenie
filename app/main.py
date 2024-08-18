from fastapi import FastAPI

from app.api.v1.routes import routers as v1_router
from app.utils.init_db import create_tables
from app.utils.rag.rag_graph import app as rag_app

app = FastAPI(
    # redoc_url=None,
    # docs_url=None,
)

create_tables()

app.include_router(v1_router, prefix="/v1")


@app.get("/")
def read_root():

    inputs = {"question": "What is the AlphaCodium paper about?"}
    try:
        # Run the RAG app
        for output in rag_app.stream(inputs, {"recursion_limit": 1}):
            for key, value in output.items():
                print(f"Node '{key}':")
            print("\n---\n")
    except Exception as e:
        print(e)
