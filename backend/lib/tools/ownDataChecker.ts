import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm } from "../chatbot";
import { JsonOutputParser, StringOutputParser } from "@langchain/core/output_parsers";
import { prisma } from "../../prisma";

const OWN_DATA_CHECKER_TEMPLATE = `
You are tasked with validating and executing an SQL statement provided by a user. The SQL statement must comply with the following rules:

1. **Scope Restriction**: The statement can only modify or affect relationships to other tables related to the specified user. It must not update or delete the user itself or other unrelated records in the database.
2. **Validation Input**: You are provided:
   - **SQL Statement**: {sql_statement}
   - **User JSON**:
\`\`\`json
{user_json}
\`\`\`
This JSON contains information about the user, including their user_id and their relationships to other tables.

Validate the SQL statement as follows:
1. Parse the JSON to extract the user_id and relationship details.
2. Analyze the SQL statement to ensure it targets only rows in related tables corresponding to the user_id or its relationships. Any action outside these bounds must be flagged.
3. If the SQL statement is valid, return in JSON format the user_id and the list of related tables that will be affected by the statement in this format:
{{
    "user_id": 123,
    "tables": ["user_roles", "permissions"]
}}
4. If the SQL statement is invalid, return an error message indicating the issue with the key "error" in this format:
{{
    "error": ...
}}
5. If the SQL statement is valid, just return the key "error" with a value of null.
6. Users of role "MANAGER" can insert or update properties and bookings.
7. Just return plain json, don't do it in markdown format.
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
    booking: {
      select: {
        id: true
      }
    },
    maintenance_request: {
      select: {
        id: true
      }
    },
    maintenance_request_update: {
      select: {
        id: true
      }
    },
    property: {
      select: {
        id: true
      }
    },
  }
})

// console.log(await ownDataChecker.invoke({
//   sql_statement: "INSERT INTO Booking (id, status) VALUES ('cm2hejqr000009o2m2snq4l86', 'pending'); UPDATE Property SET status = 'confirmed' WHERE id = 'cm2hejqr000009o2m2snq4l86';",
//   user_json: JSON.stringify(ids),
// }))

export { ownDataChecker }