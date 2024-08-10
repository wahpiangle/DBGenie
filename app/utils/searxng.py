import requests

searxngUrl = "localhost:8080"

class SearxngResult:
    def __init__(self, title: str, url: str, thumbnail = None, content = None):
        self.title = title
        self.url = url
        self.thumbnail = thumbnail
        self.content = content

def searchSearxng(query: str):
    url = f"http://{searxngUrl}/search?format=json&q={query}"
    response = requests.get(url)
    if response.status_code == 200:
        data = response.json()
        results = [
            SearxngResult(
            title=result["title"],
            url=result["url"],
            thumbnail=result.get("thumbnail"),
            content=result.get("content")
            )
            for result in data.get("results", [])[:5]
        ]
        return results
    else:
        return None


print(searchSearxng("fastapi"))