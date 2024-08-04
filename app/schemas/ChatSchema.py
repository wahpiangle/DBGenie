from datetime import datetime
from pydantic import BaseModel

class ChatBase(BaseModel):
    pass


class ChatCreate(ChatBase):
    pass
class Chat(ChatBase):
    id: int
    created_at: datetime

    class Config:
        orm_mode = True
