from langchain.callbacks.streaming_stdout import StreamingStdOutCallbackHandler
from langchain_core.callbacks import CallbackManager
from langchain_community.llms.ollama import Ollama

llm = Ollama(
    # model="llama3.1:8b",
    model="phi3:mini",
    callback_manager=CallbackManager([StreamingStdOutCallbackHandler()]),
)
