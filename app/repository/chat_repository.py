from sqlalchemy.orm import Session
from app.model.chat import Chat
from app.schemas.chat_schema import ChatInDb, ChatOutput

class ChatRepository():
    def __init__(self, db: Session):
        self.db = db
        
    def get_chats(self, skip: int = 0, limit: int = 100) -> list[ChatOutput]:
        return self.db.query(Chat).offset(skip).limit(limit).all()
    
    def create_chat(self) -> ChatInDb:
        chat = Chat()
        self.db.add(chat)
        self.db.commit()
        self.db.refresh(chat)
        return chat

    def get_chat(self, chat_id: int) -> ChatInDb:
        return self.db.query(Chat).filter(Chat.id == chat_id).first()