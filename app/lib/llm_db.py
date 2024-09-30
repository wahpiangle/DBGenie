import os
from langchain_community.utilities import SQLDatabase
from dotenv import load_dotenv
load_dotenv()

db = SQLDatabase.from_uri(os.getenv("POSTGRES_URL"))

print(db.get_usable_table_names())