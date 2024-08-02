from fastapi import Depends, FastAPI, HTTPException
from sqlalchemy.orm import Session

from app.schemas.ChatSchema import Chat, ChatCreate
from app.schemas.UserSchema import User, UserCreate

from . import crud, models
from .database import SessionLocal, engine

models.Base.metadata.create_all(bind=engine)

app = FastAPI()


# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


@app.post("/users/", response_model=User)
def create_user(user: UserCreate, db: Session = Depends(get_db)):
    db_user = crud.get_user_by_email(db, email=user.email)
    print(db_user)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    return crud.create_user(db=db, user=user)

@app.get("/users/{user_id}", response_model=User)
def read_user(user_id: int, db: Session = Depends(get_db)):
    db_user = crud.get_user(db, user_id=user_id)
    if db_user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return db_user


@app.post("/users/{user_id}/chats/", response_model=Chat)
def create_chat_for_user(
    user_id: int, chat: ChatCreate, db: Session = Depends(get_db)
):
    return crud.create_user_chat(db=db, chat=chat, user_id=user_id)


@app.get("/chats/", response_model=list[Chat])
def read_chats(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    chats = crud.get_chats(db, skip=skip, limit=limit)
    return chats