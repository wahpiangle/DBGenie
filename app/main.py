from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.v1.routes import routers as v1_router
from app.utils.init_db import create_tables


app = FastAPI(
    # redoc_url=None,
    # docs_url=None,
)
origins = [
    "http://localhost",
    "http://localhost:3000",
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
create_tables()

app.include_router(v1_router, prefix="/v1")
