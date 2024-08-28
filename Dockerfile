FROM python:3.12

WORKDIR /app

COPY ./requirements_mac.txt ./app/requirements.txt

RUN apt-get update
RUN apt-get install gcc python3-dev musl-dev libc-dev libffi-dev -y

RUN pip install --no-cache-dir --upgrade -r ./app/requirements.txt
ENV POSTGRES_URL=postgresql+psycopg://postgres:admin@postgres:5432/fyp
COPY ./app ./app

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]

EXPOSE 8000