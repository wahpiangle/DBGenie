import type { SqlDatabase } from "langchain/sql_db"

const tableColumnGenerator = (db: SqlDatabase) => {
    const allTables = db.allTables.map((t) => t.tableName);
    const tableColumns = db.allTables.map((t) => t.columns);
    return allTables.map((table, index) => {
        return `Table: ${table}
        Columns: ${tableColumns[index].map((column) => `${column.columnName} (${column.dataType}${column.isNullable ? "" : " not null"})`).join(", ")}
    `
    }).join("\n")
}

export { tableColumnGenerator }