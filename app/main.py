from fastapi import Depends, FastAPI, HTTPException
from sqlalchemy.orm import Session

from .schemas import ChatSchema
from .db.database import SessionLocal, engine
from .db import models
from .crud.ChatCRUD import get_chats

models.Base.metadata.create_all(bind=engine)

app = FastAPI()


# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/chats/", response_model=list[ChatSchema.Chat])
def read_chats(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    chats = get_chats(db, skip=skip, limit=limit)
    return chats