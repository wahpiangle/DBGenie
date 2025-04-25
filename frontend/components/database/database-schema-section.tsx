"use client";

import { Background, ReactFlow } from "@xyflow/react";
import { DatabaseSchemaNode } from "@/components/database/database-schema-node";
import { databaseInfo } from "@/types/types";

const nodeTypes = {
    databaseSchema: DatabaseSchemaNode,
};
const generateUniquePosition = (existingPositions: Set<unknown>, min = -800, max = 800) => {
    let position;
    do {
        position = {
            x: Math.floor(Math.random() * (max - min + 1)) + min,
            y: Math.floor(Math.random() * (max - min + 1)) + min,
        };
    } while (existingPositions.has(`${position.x},${position.y}`)); // Ensure uniqueness

    existingPositions.add(`${position.x},${position.y}`);
    return position;
};

export default function DatabaseSchemaSection({ databaseInfo }: { databaseInfo: databaseInfo }) {
    const existingPositions = new Set();

    const nodes = databaseInfo.tables.map((table) => {
        const position = generateUniquePosition(existingPositions);
        return {
            id: table.table_name,
            position,
            type: "databaseSchema",
            data: {
                label: table.table_name,
                schema: table.columns.map((column) => ({
                    title: column.name,
                    type: column.type,
                })),
            },
        };
    });
    const edges = databaseInfo.relationships.map((relationship) => ({
        id: `${relationship.from}-${relationship.to}`,
        source: `${relationship.from}`,
        target: `${relationship.to}`,
        sourceHandle: `${relationship.fromColumn}`,
        targetHandle: `${relationship.toColumn}`,
    }))
    return (
        <ReactFlow
            defaultNodes={nodes}
            defaultEdges={edges}
            nodeTypes={nodeTypes}
        >
            <Background />
        </ReactFlow>
    );
}