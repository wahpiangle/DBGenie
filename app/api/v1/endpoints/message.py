from fastapi import APIRouter, Depends
from fastapi.responses import StreamingResponse
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

def rag_stream(info):
    # rag_app.invoke(info, {"recursion_limit": 4})
    for output in rag_app.stream(info, {"recursion_limit": 4}):
        for node_name, node_results in output.items():
            chunk_messages = node_results.get("messages", [])
            for message in chunk_messages:
                data_str = f"data: {message.content}"
                yield f"{data_str}\n"

@router.post("/")
async def create_message(
    message : MessageInput,
    session: Session = Depends(get_db),
):
    # check if chat_id exists
    _chatService = ChatService(session)
    chat = _chatService.create_chat()
    _messageService = MessageService(session)
    _messageService.create_message(chat.id, message.content, message.role, message.metadata)
    info = {
        "question": message.content,
        "chat_id": chat.id,
    }
   
    return StreamingResponse(rag_stream(info), media_type="text/event-stream")