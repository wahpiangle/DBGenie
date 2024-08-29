from sqlalchemy.orm import Session
from app.model.message import Message, Role
from app.schemas.message_schema import MessageOutput

class MessageRepository():
    def __init__(self, db: Session):
        self.db = db
    
    def get_messages_by_chat_id(self, chat_id: int, skip:int = 0, limit: int = 100) -> list[MessageOutput]:
        return self.db.query(Message).filter(Message.chat_id == chat_id).offset(skip).limit(limit).all()
    
    def create_message(self, chat_id: int, content: str, role:str, metadata: dict = None) -> Message:
        role = Role(role)
        new_message = Message(chat_id=chat_id, content=content, role=role, metadata=metadata)
        self.db.add(new_message)
        self.db.commit()
        self.db.refresh(new_message)
        return new_message