import { SqlDatabase } from "langchain/sql_db";
import { DataSource } from "typeorm";
import { ChatOpenAI } from "@langchain/openai";

const llm = new ChatOpenAI({ model: "gpt-4o-mini-2024-07-18" });
const dataSource = new DataSource({
    type: "postgres",
    host: "localhost",
    port: 5432,
    username: "postgres",
    password: 'admin',
    database: "mydb"
})

const db = await SqlDatabase.fromDataSourceParams({
    appDataSource: dataSource,
})

export { db, llm };