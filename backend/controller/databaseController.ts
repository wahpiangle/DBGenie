import type { Request, Response } from "express";
import { prisma } from "../prisma";
import pg from 'pg'
import format from 'pg-format';

export class DatabaseController {
    public static async getDatabaseTables(req: Request, res: Response) {
        const hiddenTables = [
            'session',
            'checkpoints',
            'checkpoint_writes',
            'checkpoint_migrations',
            'checkpoint_blobs',
        ]

        const tablesWithColumnQuery = format(`SELECT
                table_name,
                column_name,
                data_type
            FROM information_schema.columns
            WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
            AND table_name NOT IN (%L)
            ORDER BY table_name, ordinal_position;`, hiddenTables);
        const foreignKeysQuery = `
            SELECT
            o.conname AS constraint_name,
            (SELECT nspname FROM pg_namespace WHERE oid=m.relnamespace) AS source_schema,
            m.relname AS source_table,
            (SELECT a.attname FROM pg_attribute a WHERE a.attrelid = m.oid AND a.attnum = o.conkey[1] AND a.attisdropped = false) AS source_column,
            (SELECT nspname FROM pg_namespace WHERE oid=f.relnamespace) AS target_schema,
            f.relname AS target_table,
            (SELECT a.attname FROM pg_attribute a WHERE a.attrelid = f.oid AND a.attnum = o.confkey[1] AND a.attisdropped = false) AS target_column
            FROM
            pg_constraint o LEFT JOIN pg_class f ON f.oid = o.confrelid LEFT JOIN pg_class m ON m.oid = o.conrelid
            WHERE
            o.contype = 'f' AND o.conrelid IN (SELECT oid FROM pg_class c WHERE c.relkind = 'r');
        `;

        const tables = await prisma.$queryRawUnsafe(tablesWithColumnQuery) as any;
        const foreignKeys = await prisma.$queryRawUnsafe(foreignKeysQuery) as any;
        res.json({
            tables:
                Object.values(
                    tables.reduce(
                        (acc: Record<string,
                            {
                                table_name: string; columns:
                                {
                                    name: string;
                                    type: string
                                }[]
                            }>,
                            row: {
                                table_name: string;
                                column_name: string;
                                data_type: string;
                            }) => {
                            if (!acc[row.table_name]) {
                                acc[row.table_name] = { table_name: row.table_name, columns: [] };
                            }
                            acc[row.table_name].columns.push({ name: row.column_name, type: row.data_type });
                            return acc;
                        },
                        {} as Record<string,
                            {
                                table_name: string;
                                columns: { name: string; type: string }[]
                            }>
                    )
                ),
            relationships: foreignKeys.map((row: any) => ({
                from: row.source_table,
                fromColumn: row.source_column,
                to: row.target_table,
                toColumn: row.target_column
            })),
        });
        return;
    }

    public static async getTableInfo(req: Request, res: Response) {
        const { Client } = pg
        const client = new Client({
            connectionString: process.env.DATABASE_URL
        })
        await client.connect()
        const { tableName } = req.query;
        const blockedTables = [
            'user',
            'session',
            'verification_token',
            'checkpoint_writes',
            'checkpoint_migrations',
            'checkpoints',
            'checkpoint_blobs',
        ]

        if (!tableName) {
            res.status(400).json({ errorMessage: "Table name is required. Please try again." });
            return;
        }

        if (blockedTables.includes(tableName as string)) {
            res.status(400).json({ errorMessage: "You are not allowed to access the specific data. Please try again." });
            return;
        }
        const query = format(`SELECT * FROM %I;`, tableName as string)
        const data = await client.query(query)
        const allTablesQuery = format(`
            SELECT table_name
            FROM information_schema.tables
            WHERE table_schema
            NOT IN ('pg_catalog', 'information_schema')
            AND table_name NOT IN (%L);`, blockedTables
        )
        const allTables = await client.query(
            allTablesQuery
        )
        await client.end()
        res.json({
            ...data,
            tables: allTables.rows
        });
        return;
    }

    public static async deleteRow(req: Request, res: Response) {
        const { Client } = pg
        const client = new Client({
            connectionString: process.env.DATABASE_URL
        })
        await client.connect()
        const { tableName, id } = req.query;
        if (!tableName || !id) {
            res.status(400).json({ errorMessage: "Table name and id are required. Please try again." });
            return;
        }
        const query = format(`DELETE FROM %I WHERE id = %L;`, tableName as string, id as string)
        const data = await client.query(query)
        await client.end()
        res.json(data);
        return;
    }
}