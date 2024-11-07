import { SqlDatabase } from "langchain/sql_db";
import { DataSource } from "typeorm";
import { llm } from "./llm";
import { createSqlQueryChain } from "langchain/chains/sql_db";
import { ChatPromptTemplate } from "@langchain/core/prompts";
import { prisma } from "../prisma";

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

console.log(db.allTables.map((t) => t.tableName));

const sqlQueryChain = await createSqlQueryChain({
    llm,
    db,
    dialect: "postgres",
});

console.log(await prisma.$executeRawUnsafe("SELECT * FROM User"));
export { sqlQueryChain };