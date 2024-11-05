import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../llm";
import { StringOutputParser } from "@langchain/core/output_parsers";
import { prisma } from "../../prisma";

const OWN_DATA_CHECKER_TEMPLATE = `
"You are given an SQL query and a list of IDs from different tables. Your task is to determine if the query affects only the specified IDs.

To do this, check if the query targets the relevant tables and includes conditions (e.g., WHERE clauses) that restrict its actions to the provided IDs. Ensure no other records are affected outside of the specified IDs.

Return True if the query exclusively affects the given IDs; otherwise, return False.

Input:
SQL Query: {sql_statement}
List of table names and corresponding IDs: {table_ids}
`

const ownDataCheckerPrompt = ChatPromptTemplate.fromTemplate(
    OWN_DATA_CHECKER_TEMPLATE
);

const ownDataChecker = ownDataCheckerPrompt.pipe(llm).pipe(new StringOutputParser());


const ids = await prisma.user.findUnique({
    where: {
        id: 'cm2hejqr000009o2m2snq4l86'
    },
    select: {
        Booking: {
            select: {
                id: true
            }
        },
        maintenanceRequest: {
            select: {
                id: true
            }
        },
        maintenanceRequestUpdate: {
            select: {
                id: true
            }
        },
        Property: {
            select: {
                id: true
            }
        },
    }
})
// console.log(await ownDataChecker.invoke({
//     sql_statement: "SELECT * FROM users WHERE username = 'admin' AND password = 'password' OR '1'='1';",
//     table_ids: [
//         {
//             table: "users",
//             ids: ["1", "2", "3"]
//         },
//         {
//             table: "orders",
//             ids: ["4", "5"]
//         }
//     ]
// }))

export { ownDataChecker }