from app.model.chat import Chat
from app.model.message import Message
from app.config.database import engine

def create_tables():
    Chat.metadata.create_all(bind=engine)
    Message.metadata.create_all(bind=engine)