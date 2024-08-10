from fastapi import APIRouter

from app.api.v1.endpoints.chat import router as chat_router
from app.api.v1.endpoints.message import router as message_router

routers = APIRouter()
router_list = [chat_router, message_router]

for router in router_list:
    router.tags = routers.tags.append("v1")
    routers.include_router(router)