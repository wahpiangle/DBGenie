import enum
from sqlalchemy import Column, DateTime, Enum, ForeignKey, Integer, String
from sqlalchemy.orm import relationship
import datetime

from .database import Base

class Role(enum.Enum):
    user = "user"
    bot = "bot"

class Chat(Base):
    __tablename__ = "chats"

    id = Column(Integer, primary_key=True)
    created_at = Column(DateTime, default=datetime.datetime.now)

class Message(Base):
    __tablename__ = "messages"

    id = Column(Integer, primary_key=True)
    content = Column(String)
    chat_id = Column(Integer, ForeignKey("chats.id"))
    role = Column(Enum(Role))
    metadata_ = Column("metadata", String)

    chat = relationship("Chat", back_populates="messages")
