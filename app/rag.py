import bs4
from langchain.callbacks.streaming_stdout import StreamingStdOutCallbackHandler
from langchain_core.callbacks import CallbackManager
from langchain import hub
from langchain_community.llms import Ollama
from langchain_core.runnables import RunnablePassthrough
from langchain_core.output_parsers import StrOutputParser
from vector_store import vectorstore
from langchain_community.document_loaders import WebBaseLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter

bs4_strainer = bs4.SoupStrainer()
loader = WebBaseLoader(
    web_paths=("https://jina.ai/reader/",),
    bs_kwargs={"parse_only": bs4_strainer},
)
docs = loader.load()
text_splitter = RecursiveCharacterTextSplitter(chunk_size=1000, chunk_overlap=200)
splits = text_splitter.split_documents(docs)

vectorstore.add_documents(splits)

# Retrieve and generate using the relevant snippets of the blog.
retriever = vectorstore.as_retriever()
prompt = hub.pull("rlm/rag-prompt")

llm = Ollama(
    model="llama3.1:8b",
    callback_manager=CallbackManager([StreamingStdOutCallbackHandler()]),
)
def format_docs(docs):
    return "\n\n".join(doc.page_content for doc in docs)


rag_chain = (
    {"context": retriever | format_docs, "question": RunnablePassthrough()}
    | prompt
    | llm
    | StrOutputParser()
)

llm.invoke("what does jina ai do")