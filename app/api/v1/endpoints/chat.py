from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.config.database import get_db
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