from datetime import datetime
from pydantic import BaseModel

from app.schemas.MessagesSchema import MessageOutput

# class ChatBase(BaseModel):
#     pass


# class ChatCreate(ChatBase):
#     pass
# class Chat(ChatBase):
#     id: int
#     created_at: datetime

#     class Config:
#         orm_mode = True
class ChatInput(BaseModel):
    pass

class ChatInDb(BaseModel):
    id: int
    created_at: datetime

    class Config:
        orm_mode = True

class ChatOutput(BaseModel):
    id: int
    created_at: datetime
    messages: list[MessageOutput]