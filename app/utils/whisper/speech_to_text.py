import whisper
from pathlib import Path

def get_speech_as_text(file_name: str) -> str:
    storage_folder = Path(__file__).parent.parent.parent / "storage"
    file_to_open = storage_folder / file_name
    model = whisper.load_model("tiny")
    result = model.transcribe(file_to_open)
    return result['text']
