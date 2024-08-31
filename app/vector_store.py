# Setup Embeddings
from pathlib import Path
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_postgres import PGVector
from dotenv import load_dotenv
import os
from langchain_unstructured import UnstructuredLoader

load_dotenv() 

modelPath = "Alibaba-NLP/gte-base-en-v1.5"
model_kwargs = { "device": "cpu", "trust_remote_code": True }
encode_kwargs = {'normalize_embeddings': True}
hugging_face_embeddings = HuggingFaceEmbeddings(
    model_name=modelPath,     # Provide the pre-trained model's path
    model_kwargs=model_kwargs, # Pass the model configuration options
    encode_kwargs=encode_kwargs, # Pass the encoding options
)

vectorstore = PGVector(
    embeddings=hugging_face_embeddings,
    collection_name="vector_store",
    # postgresql+psycopg://postgres:admin@localhost:5432/fyp
    connection=os.getenv("POSTGRES_URL"),
)

def load_document(file_name: str):
    storage_folder = Path(__file__).parent / "storage"
    file_to_open = storage_folder / file_name
    loader = UnstructuredLoader(file_to_open)
    docs = loader.load()
    vectorstore.add_documents(docs)