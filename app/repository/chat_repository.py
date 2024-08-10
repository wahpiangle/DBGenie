from sqlalchemy.orm import Session
from app.model.chat import Chat
from app.schemas.ChatSchema import ChatInput, ChatInDb

class ChatRepository():
    def __init__(self, db: Session):
        self.db = db
        
    def get_chats(self, skip: int = 0, limit: int = 100):
        return self.db.query(Chat).offset(skip).limit(limit).all()
    
    def create_chat(self, chat: ChatInput) -> ChatInDb:
        chat = Chat(**chat.model_dump(exclude_none=True))
        self.db.add(chat)
        self.db.commit()
        self.db.refresh(chat)
        return chat