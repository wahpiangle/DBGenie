import datetime
from sqlalchemy import Column, DateTime, Integer

from app.config.database import Base

class Chat(Base):
    __tablename__ = "chats"

    id = Column(Integer, primary_key=True)
    created_at = Column(DateTime, default=datetime.datetime.now)