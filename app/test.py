from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_community.document_loaders import FireCrawlLoader
import requests
from vector_store import vectorstore

firecrawlUrl = "http://localhost:3002"

def removeCompletedResults():
    """
    Remove completed results from the web-scraper queue from self-hosted FireCrawl instance
    """
    requests.put(f"{firecrawlUrl}/admin/@/queues/api/queues/web-scraper/clean/completed")

def crawl_and_save_docs(url: str, max_depth:int = 3, limit:int = 1, mode:str = "scrape"):
    loader = FireCrawlLoader(
        api_url=firecrawlUrl,
        url=url,
        api_key=123,
        mode=mode,
        params={
            "crawlerOptions":{
                "maxDepth": max_depth,
                "limit": limit,
            },
            "pageOptions":{
                "onlyMainContent": True,
            }
        }
    )
    docs = loader.load()
    text_splitter = RecursiveCharacterTextSplitter(chunk_size=1000, chunk_overlap=200)
    splits = text_splitter.split_documents(docs)
    vectorstore.add_documents(splits)
    removeCompletedResults()

crawl_and_save_docs("https://fastapi.tiangolo.com/", mode="scrape")