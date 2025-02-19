import type { Request, Response } from "express";
import { prisma } from "../prisma";

export class DatabaseController {
    public static async getDatabaseTables(req: Request, res: Response) {
        const tablesWithColumnQuery = `
            SELECT
                table_name,
                column_name,
                data_type
            FROM information_schema.columns
            WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
            ORDER BY table_name, ordinal_position;
        `;
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
                        (acc: Record<string, { table_name: string; columns: { name: string; type: string }[] }>, row: { table_name: string; column_name: string; data_type: string; }) => {
                            if (!acc[row.table_name]) {
                                acc[row.table_name] = { table_name: row.table_name, columns: [] };
                            }
                            acc[row.table_name].columns.push({ name: row.column_name, type: row.data_type });
                            return acc;
                        },
                        {} as Record<string, { table_name: string; columns: { name: string; type: string }[] }>
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
}