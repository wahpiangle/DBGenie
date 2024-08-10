from fastapi import FastAPI

from app.api.v1.routes import routers as v1_router
from app.utils.init_db import create_tables

app = FastAPI()

create_tables()

app.include_router(v1_router, prefix="/v1")