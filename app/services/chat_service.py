from requests import Session
from app.repository.chat_repository import ChatRepository


class ChatService():
    def __init__(self, session:Session):
        self.chat_repository = ChatRepository(session)

    def get_chats(self, skip: int = 0, limit: int = 100):
        return self.chat_repository.get_chats(skip, limit)

    def create_chat(self, chat):
        return self.chat_repository.create_chat(chat)