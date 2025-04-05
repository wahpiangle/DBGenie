import { Annotation } from "@langchain/langgraph"
import { db } from "./chatbot"
import { questionEvaluation, type QuestionEvaluatorOutput } from "./tools/questionEvaluator"
import { prisma } from "../prisma"
import { Role, type user } from "@prisma/client"
import { userQueryChecker } from "./tools/userQueryChecker"
import { determineExecuteOrQuery } from "./tools/determineExecuteOrQuery"
import { injectionPreventionChecker } from "./tools/injectionPrevention"
import { createId } from '@paralleldrive/cuid2';
import { readQueryGenerator } from "./tools/readQueryGenerator"
import { ownDataChecker } from "./tools/ownDataChecker"
import successExecuteMessageGeneration from "./tools/successExecuteMessageGeneration"
import { SQLStatementGenerator } from "./tools/generateSQLStatement"
import { determineAlterSchema } from "./tools/determineAlterSchema"
import { queryPg } from "./pgdb"
import { determineValidQuery } from "./tools/determineValidQuery"

const GraphState = Annotation.Root({
    question: Annotation<string>,
    generation: Annotation<string>,
    user: Annotation<user>,
    errorMessage: Annotation<string>,
    result: Annotation<string>,
    alterSchema: Annotation<boolean>,
    messages: Annotation<{
        role: string;
        content: string;
        created_at: Date;
    }[]>,
})

const determineUserQueryIsValid = async (state: typeof GraphState.State) => {
    console.log("====== Checking if the user query is valid ======")
    const result = await determineValidQuery.invoke({
        user_id: state.user.id,
        database_schema: db.allTables,
        user_query: state.question,
        sql_statement: state.generation,
        conversation_history: state.messages?.map((message) => ({
            role: message.role,
            content: message.content,
        })) ?? [],
    })
    if (result.split(' ')[0].toLowerCase().includes('yes')) {
        return {}
    } else {
        return { errorMessage: result }
    }
}

const generateSqlQuery = async (state: typeof GraphState.State): Promise<Partial<typeof GraphState.State>> => {
    console.log("Generating SQL query for question:", state.question)
    console.log("The messages state is:", state.messages)
    const generatedQuery = await SQLStatementGenerator.invoke({
        generated_id: createId(),
        database_schema: db.allTables,
        user_query: state.question,
        user_id: state.user.id,
        conversation_history: state.messages?.map((message) => ({
            role: message.role,
            content: message.content,
        })) ?? [],
    })
    const extractSQL = (input: string) => {
        const regex = /```sql\n([\s\S]*?)\n```/;
        const match = input.match(regex);
        return match ? match[1] : input;
    };
    const removeThinkTag = (input: string) => {
        return input.replace(/<think>[\s\S]*?<\/think>/g, "")
    }
    console.log("Generated SQL query:", generatedQuery)
    console.log("Extracted SQL query:", removeThinkTag(extractSQL(generatedQuery)))
    return {
        generation: removeThinkTag(extractSQL(generatedQuery)),
        messages: [
            ...(state.messages || []),
            {
                role: "human",
                content: state.question,
                created_at: new Date()
            }
        ],
    }
}

const checkAlterTableSchema = async (state: typeof GraphState.State) => {
    console.log("====== Checking if the user is trying to update the table schema ======")
    const response = await determineAlterSchema.invoke({
        sql_statement: state.generation
    });

    return ({
        alterSchema: (response.split(' ')[0].toLowerCase() === 'yes')
    });
}

const checkUserQueryOwnData = async (state: typeof GraphState.State) => {
    console.log("======== Checking if the user is updating or deleting a record that does not belong to them ========")
    const user = state.user
    await queryPg(`
        CREATE OR REPLACE FUNCTION get_related_ids(user_id_value TEXT)
        RETURNS TABLE(id TEXT, table_name TEXT)
        LANGUAGE plpgsql AS $$
        DECLARE
            rec RECORD;
            query TEXT := '';
        BEGIN
            FOR rec IN (
                SELECT conrelid::regclass AS table_name, attname AS column_name
                FROM pg_constraint c
                JOIN pg_attribute a
                    ON a.attnum = ANY(c.conkey)
                    AND a.attrelid = c.conrelid
                WHERE c.confrelid = 'user'::regclass
            ) LOOP
                query := query || format(
                    'SELECT id::TEXT, %L AS table_name FROM %I WHERE %I = %L UNION ALL ',
                    rec.table_name, rec.table_name, rec.column_name, user_id_value
                );
            END LOOP;

            IF query != '' THEN
                query := left(query, length(query) - 11);  -- Remove last ' UNION ALL '
                RETURN QUERY EXECUTE query;
            ELSE
                RETURN;
            END IF;
        END $$;
    `);

    const ids = await queryPg(`SELECT * FROM get_related_ids('${user.id}');`);
    const idsMap = Object.values(
        ids.rows.reduce((acc, { table_name, id }) => {
            if (!acc[table_name]) {
                acc[table_name] = { table_name, id: [] };
            }
            acc[table_name].id.push(id);
            return acc;
        }, {})
    );
    const response = await ownDataChecker.invoke({
        sql_statement: state.generation,
        user_json: JSON.stringify(idsMap)
    }) as { error: string, valid: boolean }
    if (!response?.valid) {
        return { errorMessage: "You are not allowed to access the specific data. Please try again." }
    }
}

const blockTables = async (state: typeof GraphState.State) => {
    console.log("====== Blocking tables ======")
    const userQuery = await userQueryChecker.invoke({
        blockedTables: ['Users', 'VerificationToken', 'Session'].join(', '),
        sql_statement: state.generation
    })
    if (userQuery.split(' ')[0].toLowerCase() === 'yes') {
        return { errorMessage: "You are not allowed to access the specific data. Please try again." }
    }
    return;
}

const injectionPrevention = async (state: typeof GraphState.State) => {
    console.log("====== Checking for SQL injection ======")
    const sqlQuery = state.generation
    const isMalicious = await injectionPreventionChecker.invoke({
        sql_statement: sqlQuery
    })
    if (isMalicious.split(' ')[0].toLowerCase() === 'yes') {
        return {
            errorMessage: "The query is invalid and cannot be processed. Please try again."
        }
    }
    return;
}

const evaluateSufficientInfo = async (state: typeof GraphState.State) => {
    console.log(`====== Evaluating if the user's query has sufficient information ======`)
    const hasSufficientInfo = await questionEvaluation.invoke({
        input: state.question,
        sql_statement: state.generation,
        table: db.allTables
    }) as QuestionEvaluatorOutput

    if (hasSufficientInfo.evaluation === 'Insufficient') {
        console.log("====== QUERY INSUFFICIENT INFORMATION ======")
        return { errorMessage: hasSufficientInfo.feedback }
    } else {
        console.log("====== QUERY SUFFICIENT INFORMATION ======")
    }
    return
}

const blockUserFromAlteringSchema = async (state: typeof GraphState.State) => {
    console.log("Blocking user from altering schema")
    if (state.user.role === Role.USER && state.alterSchema) {
        return { errorMessage: "You are not allowed to alter the database schema. Please try again." }
    } else {
        return {}
    }
}

const runQueryToDb = async (state: typeof GraphState.State) => {
    console.log("Running query to database: ", state.generation)
    const isQuery = await determineExecuteOrQuery.invoke({
        sql_statement: state.generation
    })
    try {

        const isReadQuery = isQuery.split(' ')[0].toLowerCase() === 'yes';
        const queryResult = isReadQuery
            ? await queryPg(state.generation)
            : await prisma.$executeRawUnsafe(state.generation);

        const resultMessage = isReadQuery
            ? await readQueryGenerator.invoke({ sql_statement: state.generation, result: queryResult })
            : await successExecuteMessageGeneration.invoke({ sql_statement: state.generation });

        return {
            result: resultMessage,
            messages: [
                ...state.messages,
                { role: "assistant", content: resultMessage, created_at: new Date() }
            ]
        }
    }
    catch (e) {
        console.log("Error:", e)
        return {
            errorMessage: "The query is invalid and cannot be processed. Please try again."
        }
    }
}

const generateErrorMessage = async (state: typeof GraphState.State) => {
    console.log("Generating error message")
    console.log("The state is:", state)
    return {
        result: state.errorMessage,
        messages: [
            ...state.messages,
            { role: "assistant", content: state.errorMessage, created_at: new Date() }
        ]
    }
}

const decideToReject = async (state: typeof GraphState.State) => {
    if (state.errorMessage) {
        return "reject"
    } else {
        return "accept"
    }
}

const decideAlterSchema = async (state: typeof GraphState.State) => {
    if (state.alterSchema) {
        return "alter"
    } else {
        return "not alter"
    }
}

const resetState = async (state: typeof GraphState.State) => {
    return {
        errorMessage: "",
        generation: "",
        result: "",
    }
}

export {
    GraphState,
    generateSqlQuery,
    checkAlterTableSchema,
    determineUserQueryIsValid,
    checkUserQueryOwnData,
    blockUserFromAlteringSchema,
    blockTables,
    evaluateSufficientInfo,
    runQueryToDb,
    generateErrorMessage,
    injectionPrevention,
    decideToReject,
    decideAlterSchema,
    resetState,
}