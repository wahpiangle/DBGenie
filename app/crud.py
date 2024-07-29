from sqlalchemy.orm import Session
from . import models, schema

def get_chat(db: Session, chat_id: int):
    return db.query(models.Chat).filter(models.Chat.id == chat_id).first()

def create_chat(db: Session, chat: schema.ChatCreate):
    db_chat = models.Chat(message_id=chat.message_id)
    db.add(db_chat)
    db.commit()
    db.refresh(db_chat)
    return db_chat