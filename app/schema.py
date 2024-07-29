from pydantic import BaseModel

class MessageBase(BaseModel):
    content: str

class MessageCreate(MessageBase):
    pass

class Message(MessageBase):
    id: int

    class Config:
        orm_mode = True


class ChatBase(BaseModel):
    message_id: int

class ChatCreate(ChatBase):
    pass

class Chat(ChatBase):
    id: int

    class Config:
        orm_mode = True