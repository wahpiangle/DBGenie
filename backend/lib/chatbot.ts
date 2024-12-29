import { SqlDatabase } from "langchain/sql_db";
import { DataSource } from "typeorm";
import { llm } from "./llm";
import { createSqlQueryChain } from "langchain/chains/sql_db";

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

const sqlQueryChain = await createSqlQueryChain({
    llm,
    db,
    dialect: "postgres",
});

export { sqlQueryChain, db };