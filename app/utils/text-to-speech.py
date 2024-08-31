from pathlib import Path
from TTS.api import TTS
tts = TTS("tts_models/multilingual/multi-dataset/xtts_v2", gpu=False)

# generate speech by cloning a voice using default settings

def generate_speech(text: str, speaker_wav_file_name: str = "female.wav", language: str = "en", output_name: str = "output.wav") -> str:
    storage_folder = Path(__file__).parent.parent / "storage" / "tts"
    tts.tts_to_file(text=text,
                    file_path=storage_folder / "output" / output_name,
                    speaker_wav=storage_folder / speaker_wav_file_name,
                    language=language)
    return str(storage_folder / "output" / output_name)

# generate_speech("I have no mouth and I must scream! This is a test of the text to speech system.")