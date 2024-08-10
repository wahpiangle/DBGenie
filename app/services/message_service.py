from requests import Session

from app.repository.message_repository import MessageRepository


class MessageService():
    def __init__(self, session:Session):
        self.message_repository = MessageRepository(session)

    def get_messages(self, skip: int = 0, limit: int = 100):
        return self.message_repository.get_messages(skip, limit)