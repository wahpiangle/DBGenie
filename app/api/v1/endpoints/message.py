from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.config.database import get_db
from app.schemas.message_schema import MessageInput
from app.services.chat_service import ChatService
from app.services.message_service import MessageService
from app.utils.rag.rag_graph import app as rag_app

router = APIRouter(
    prefix="/message",
    tags=["message"],
)

@router.get("/{chat_id}")
def get_messages_by_chat_id(
    chat_id: int,
    session: Session = Depends(get_db),
    skip: int = 0,
    limit: int = 100
):
    _messageService = MessageService(session)
    return _messageService.get_messages_by_chat_id(chat_id, skip, limit)

@router.post("/{chat_id}")
def create_message(
    chat_id: int,
    message : MessageInput,
    session: Session = Depends(get_db),
):
    # check if chat_id exists
    _chatService = ChatService(session)
    chat = _chatService.get_chat(chat_id) or _chatService.create_chat()
    _messageService = MessageService(session)
    _messageService.create_message(chat.id, message.content, message.role, message.metadata)
    response = rag_app.invoke(
        {
            "question": message.content,
            "chat_id": chat.id,
        },
        {"recursion_limit": 4})
    print(response)
    return None
    # return res