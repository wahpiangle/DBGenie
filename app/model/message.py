import enum

from sqlalchemy import Column, Enum, ForeignKey, Integer, String
from sqlalchemy.orm import relationship

from app.config.database import Base


class Role(enum.Enum):
    user = "user"
    bot = "bot"



class Message(Base):
    __tablename__ = "messages"

    id = Column(Integer, primary_key=True)
    content = Column(String)
    chat_id = Column(Integer, ForeignKey("chats.id"))
    role = Column(Enum(Role))
    metadata_ = Column("metadata", String)

    chat = relationship("Chat", back_populates="messages")