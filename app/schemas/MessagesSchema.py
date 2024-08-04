from pydantic import BaseModel
from app.db.models import Role

class MessageBase(BaseModel):
    pass

class MessageCreate(MessageBase):
    content: str
    role: Role

class Message(MessageBase):
    id: int
    content: str
    chat_id: int
    role: Role
    metadata: str
    
    class Config:
        orm_mode = True