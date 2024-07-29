// modelPath = "Alibaba-NLP/gte-base-en-v1.5"
// model_kwargs = { "device": "cpu", "trust_remote_code": True }
// encode_kwargs = {'normalize_embeddings': True}
// hugging_face_embeddings = HuggingFaceEmbeddings(
//     model_name=modelPath,     # Provide the pre-trained model's path
//     model_kwargs=model_kwargs, # Pass the model configuration options
//     encode_kwargs=encode_kwargs, # Pass the encoding options
// )

// vectorstore = PGVector(
//     embeddings=hugging_face_embeddings,
//     collection_name="test_docs",
//     connection="postgresql+psycopg://postgres:admin@localhost:5432/fyp",
// )

import {
    type DistanceStrategy,
    PGVectorStore,
} from "@langchain/community/vectorstores/pgvector";
import type { PoolConfig } from "pg";
import { HuggingFaceTransformersEmbeddings } from "@langchain/community/embeddings/hf_transformers";

const embeddings = new HuggingFaceTransformersEmbeddings({
    model: "Alibaba-NLP/gte-base-en-v1.5",
});


const config = {
    postgresConnectionOptions: {
        type: "postgres",
        host: "127.0.0.1",
        port: 5432,
        user: "postgres",
        password: "admin",
        database: "fyp",
    } as PoolConfig,
    tableName: "langchain",
    columns: {
        idColumnName: "id",
        vectorColumnName: "vector",
        contentColumnName: "content",
        metadataColumnName: "metadata",
    },
    // supported distance strategies: cosine (default), innerProduct, or euclidean
    distanceStrategy: "cosine" as DistanceStrategy,
};

const pgvectorStore = await PGVectorStore.initialize(
    embeddings,
    config
);

export { pgvectorStore };