from firecrawl import FirecrawlApp
class FirecrawlResponse: 
    def __init__(self, content:str, metadata: dict):
        self.content = content
        self.metadata = metadata

app=FirecrawlApp(
    api_key=123,
    api_url="http://localhost:3002"
)
crawl_result = app.crawl_url(
    url="https://fastapi.tiangolo.com",
    params={
        "crawlerOptions":{
            "maxDepth": 3,
            "limit": 10,
        },
        "pageOptions":{
            "onlyMainContent": True,
        }
    }
)

# Get the markdown
for result in crawl_result:
    result = FirecrawlResponse(result["content"], result["metadata"])
    with open("output.md", "w") as f:
        f.write(result.content)