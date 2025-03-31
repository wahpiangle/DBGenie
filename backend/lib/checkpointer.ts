import { PostgresSaver } from "@langchain/langgraph-checkpoint-postgres";

const checkpointer = PostgresSaver.fromConnString(
    process.env.DATABASE_URL!,
)

export default checkpointer;