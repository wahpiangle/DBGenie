import whisper
from pathlib import Path

def get_speech_as_text(file_name: str) -> str:
    model = whisper.load_model("tiny")
    result = model.transcribe(file_name, language="en")
    return result['text']