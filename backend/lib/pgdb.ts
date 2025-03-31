import pg from 'pg'
export const queryPg = async (sql: string) => {
    const { Client } = pg
    const client = new Client({
        connectionString: process.env.DATABASE_URL
    })
    await client.connect()
    const data = await client.query(sql)
    await client.end()
    return data
}

export const runMultipleQueries = async (queries: string[]) => {
    const { Client } = pg
    const client = new Client({
        connectionString: process.env.DATABASE_URL
    })
    await client.connect()
    const data = await Promise.allSettled(queries.map(query => client.query(query)))
    await client.end()
    return data
}