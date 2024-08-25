import datetime
from sqlalchemy import Column, DateTime, Integer, String

from app.config.database import Base

class Document(Base):
    __tablename__ = "documents"

    id = Column(Integer, primary_key=True)
    file_name = Column(String)
    file_path = Column(String)
    created_at = Column(DateTime, default=datetime.datetime.now)