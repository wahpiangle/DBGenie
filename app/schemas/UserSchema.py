from pydantic import BaseModel
from app.schemas.ChatSchema import Chat

class UserBase(BaseModel):
    email: str


class UserCreate(UserBase):
    password: str


class User(UserBase):
    id: int
    chats: list[Chat] = []

    class Config:
        orm_mode = True