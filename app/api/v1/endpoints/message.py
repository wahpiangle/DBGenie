from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.config.database import get_db
from app.services.message_service import MessageService


router = APIRouter(
    prefix="/message",
    tags=["message"],
)

@router.get("/")
def read_messages(
    session: Session = Depends(get_db),
):
    _messageService = MessageService(session)
    return _messageService.get_messages()
