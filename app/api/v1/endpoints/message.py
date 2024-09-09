import os
from tempfile import NamedTemporaryFile
from typing import List
from fastapi import APIRouter, Depends, File, HTTPException, UploadFile
from sqlalchemy.orm import Session
from app.config.database import get_db
from app.schemas.message_schema import MessageInput
from app.services.chat_service import ChatService
from app.services.message_service import MessageService
from app.utils.rag.rag_graph import app as rag_app
from app.utils.whisper.speech_to_text import get_speech_as_text

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

# async def rag_stream(info):
#     async for event in rag_app.astream_events(info, version="v2"):
#         kind = event["event"]
#         tags = event.get("tags", [])
#         if kind == "on_llm_stream" and ("generate_general_response" in tags):
#             data = event["data"]
#             if data["chunk"].text:
#                 yield f"data: {data['chunk'].text}\n\n"

@router.post("/")
async def create_message(
    message : MessageInput,
    session: Session = Depends(get_db),
):
    _chatService = ChatService(session)
    chat = _chatService.create_chat()
    _messageService = MessageService(session)
    _messageService.create_message(chat.id, message.content, message.role, message.metadata)
    info = {
        "question": message.content,
        "chat_id": chat.id,
    }
    return rag_app.invoke(info)['generation']

@router.post("/{chat_id}")
async def create_message_by_chat_id(
    chat_id: int,
    message : MessageInput,
    session: Session = Depends(get_db),
):
    _messageService = MessageService(session)
    _messageService.create_message(chat_id, message.content, message.role, message.metadata)
    info = {
        "question": message.content,
        "chat_id": chat_id,
    }
    return rag_app.invoke(info)['generation']

@router.post("/speech/")
async def upload_audio(file: UploadFile = File(...)):
    # create a temporary file to store the audio in app/storage
    with NamedTemporaryFile(delete=False, suffix=".wav", dir="app/storage") as temp_audio:
        temp_audio.write(file.file.read())
        result = get_speech_as_text(temp_audio.name)
        os.unlink(temp_audio.name)

    return {"transcript": result}