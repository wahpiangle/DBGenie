from langchain_community.utilities import SearxSearchWrapper

searxngUrl = "http://localhost:8080"


class SearxngResult:
    def __init__(self, title: str, url: str, content: str):
        self.title = title
        self.url = url
        self.content = content


search = SearxSearchWrapper(searx_host=searxngUrl)


def searchSearxng(query: str) -> list[SearxngResult]:
    return [
        SearxngResult(
            title=result["title"],
            content=result["snippet"],
            url=result["link"],
        )
        for result in search.results(query, num_results=3)
    ]
