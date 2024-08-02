from pydantic import BaseModel

class ChatBase(BaseModel):
    pass


class ChatCreate(ChatBase):
    pass


class Chat(ChatBase):
    id: int
    user_id: int

    class Config:
        orm_mode = True
