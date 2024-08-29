from requests import Session

from app.model.message import Role
from app.repository.message_repository import MessageRepository


class MessageService():
    def __init__(self, session:Session):
        self.message_repository = MessageRepository(session)
    
    def get_messages_by_chat_id(self, chat_id: int, skip:int = 0, limit: int =100):
        return self.message_repository.get_messages_by_chat_id(chat_id, skip, limit)
    
    def create_message(self, chat_id: int, content: str, role:str, metadata: dict = None):
        return self.message_repository.create_message(chat_id, content, role, metadata)