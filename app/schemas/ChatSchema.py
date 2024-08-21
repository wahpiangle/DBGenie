from datetime import datetime
from pydantic import BaseModel


class ChatInput(BaseModel):
    pass


class ChatInDb(BaseModel):
    id: int
    title: str
    created_at: datetime

    class Config:
        from_attributes = True


class ChatOutput(BaseModel):
    id: int
    title: str
    created_at: datetime
