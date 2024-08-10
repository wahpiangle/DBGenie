from sqlalchemy.orm import Session
from app.model.message import Message
from app.schemas.MessagesSchema import MessageOutput

class MessageRepository():
    def __init__(self, db: Session):
        self.db = db
        
    def get_messages(self, skip: int = 0, limit: int = 100) -> list[MessageOutput]:
        return self.db.query(Message).offset(skip).limit(limit).all()