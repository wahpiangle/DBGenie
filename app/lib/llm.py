from langchain.callbacks.streaming_stdout import StreamingStdOutCallbackHandler
from langchain_core.callbacks import CallbackManager
from langchain_community.llms import Ollama

llm = Ollama(
    # model="llama3.1:8b",
    model="gemma2:2b",
    callback_manager=CallbackManager([StreamingStdOutCallbackHandler()]),
)
