from pydantic import BaseModel

class DocumentOutput(BaseModel):
    id: int
    file_name: str
    file_path: str