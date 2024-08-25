from pydantic import BaseModel
from app.model.message import Role


class MessageInput(BaseModel):
    content: str
    role: Role
    metadata: dict


class MessageOutput(BaseModel):
    id: int
    content: str
    chat_id: int
    role: Role
    metadata: str

    class Config:
        from_attributes = True
