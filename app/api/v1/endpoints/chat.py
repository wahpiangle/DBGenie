from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.config.database import get_db
from app.schemas.ChatSchema import ChatInput
from app.services.chat_service import ChatService


router = APIRouter(
    prefix="/chat",
    tags=["chat"],
)

@router.get("/")
def read_chats(
    session: Session = Depends(get_db),
):
    _chatService = ChatService(session)
    return _chatService.get_chats()

@router.post("/")
def create_chat(
    chat: ChatInput,
    session: Session = Depends(get_db),
):
    _chatService = ChatService(session)
    return _chatService.create_chat(chat)