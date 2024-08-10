from contextlib import asynccontextmanager
from fastapi import Depends, FastAPI, HTTPException

from app.api.v1.routes import routers as v1_router
from app.utils.init_db import create_tables


app = FastAPI()

@asynccontextmanager
async def lifespan(app: FastAPI):
    create_tables()
    yield
    

app.include_router(v1_router, prefix="/v1")

# @app.get("/chats/", response_model=list[ChatSchema.Chat])
# def read_chats(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
#     chats = get_chats(db, skip=skip, limit=limit)
#     return chats