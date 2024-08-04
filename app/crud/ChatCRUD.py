from sqlalchemy.orm import Session

from app.schemas.ChatSchema import ChatCreate

from app.db.models import Chat

def get_chats(db: Session, skip: int = 0, limit: int = 100):
    return db.query(Chat).offset(skip).limit(limit).all()


def create_user_chat(db: Session, chat: ChatCreate):
    db_chat = Chat(**chat.model_dump())
    db.add(db_chat)
    db.commit()
    db.refresh(db_chat)
    return db_chat