from crawl4ai import WebCrawler

# Create an instance of WebCrawler
crawler = WebCrawler()

# Warm up the crawler (load necessary models)
crawler.warmup()

# Run the crawler on a URL
result = crawler.run(url="https://www.nottingham.edu.my/index.aspx")

# write the extracted content to a file
with open("nbcnews.txt", "w") as f:
    f.write(result.extracted_content)
print(result.extracted_content)