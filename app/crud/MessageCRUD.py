from sqlalchemy.orm import Session

from app.schemas.MessagesSchema import MessageCreate

from app.db.models import Message

def getMessagesByChatId(db: Session, chat_id: int, skip: int = 0, limit: int = 100):
    return db.query(Message).filter(Message.chat_id == chat_id).offset(skip).limit(limit).all()

def create_message(db: Session, message: MessageCreate, chat_id: int):
    db_message = Message(**message.model_dump(), chat_id=chat_id)
    db.add(db_message)
    db.commit()
    db.refresh(db_message)
    return db_message