import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../llm";
import { JsonOutputParser } from "@langchain/core/output_parsers";
import { prisma } from "../../prisma";

const OWN_DATA_CHECKER_TEMPLATE = `
"Given an SQL query, identify the table affected by the query and the IDs affected, if applicable. Respond only with a JSON output that includes the following fields:

table: the name of the table impacted by the query.
id_affected: a list of IDs affected (or an empty list if no IDs are specified).

If the query does not affect any specific IDs, return an empty array for id_affected. If the query affects multiple tables, return a JSON array, where each entry has the table and id_affected fields.

SQL Query: {sql_statement}
`

const ownDataCheckerPrompt = ChatPromptTemplate.fromTemplate(
    OWN_DATA_CHECKER_TEMPLATE
);

const ownDataChecker = ownDataCheckerPrompt.pipe(llm).pipe(new JsonOutputParser());


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

console.log(await ownDataChecker.invoke({
    sql_statement: "INSERT INTO Booking (id, status) VALUES ('cm2hejqr000009o2m2snq4l86', 'pending'); UPDATE Property SET status = 'confirmed' WHERE id = 'cm2hejqr000009o2m2snq4l86';"
}))

export { ownDataChecker }