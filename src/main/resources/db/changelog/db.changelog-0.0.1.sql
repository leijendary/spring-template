--liquibase formatted sql
--changeset leijendary:create-sample-table
CREATE TABLE sample (
    id VARCHAR(28) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(2000),
    amount NUMERIC(12,2) NOT NULL,
    version SMALLINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by TEXT NOT NULL,
    last_modified_at TIMESTAMP NOT NULL DEFAULT NOW(),
    last_modified_by TEXT NOT NULL
);

--changeset leijendary:create-sample-name-lower-unique-index
CREATE UNIQUE index sample_name_key ON sample(LOWER(name));

--changeset leijendary:create-sample-created-at-id-index
CREATE INDEX sample_created_at_id_idx ON sample(created_at DESC, id);

--changeset leijendary:create-sample-translation-table
CREATE TABLE sample_translation (
    id VARCHAR(28) REFERENCES sample(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    language VARCHAR(4),
    ordinal SMALLINT NOT NULL,
    CONSTRAINT sample_translation_pkey PRIMARY KEY (id, language)
);

--changeset leijendary:create-sample-translation-id-index
CREATE INDEX sample_translation_id_idx ON sample_translation(id);

--changeset leijendary:create-image-table
CREATE TABLE image (
    id VARCHAR(28) PRIMARY KEY,
    name VARCHAR(250) NOT NULL unique,
    media_type VARCHAR(15),
    validated BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by TEXT NOT NULL
);

--changeset leijendary:create-image-metadata-table
CREATE TABLE image_metadata (
    id VARCHAR(28) REFERENCES image(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    value TEXT NOT NULL,
    CONSTRAINT image_metadata_pkey PRIMARY KEY (id, name)
);

--changeset leijendary:create-image-metadata-id-index
CREATE INDEX image_metadata_id_idx ON image_metadata(id);

--changeset leijendary:create-sample-image-table
CREATE TABLE sample_image (
    id VARCHAR(28) REFERENCES sample(id) ON DELETE CASCADE PRIMARY KEY,
    original VARCHAR(250) NOT NULL REFERENCES image(name),
    preview VARCHAR(250) NOT NULL REFERENCES image(name),
    thumbnail VARCHAR(250) NOT NULL REFERENCES image(name)
);

--changeset leijendary:create-ai-chat-table
CREATE TABLE ai_chat (
    id VARCHAR(28) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by TEXT NOT NULL
);

--changeset leijendary:create-ai-chat-created-by-created-at-id-index
CREATE INDEX ai_chat_created_by_created_at_id_idx ON ai_chat(created_by, created_at DESC, id);

--changeset leijendary:create-ai-chat-memory-table
CREATE TABLE ai_chat_memory (
    conversation_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('USER', 'ASSISTANT')),
    "timestamp" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--changeset leijendary:create-ai-chat-memory-conversation-id-timestamp-index
CREATE INDEX ai_chat_memory_conversation_id_timestamp_idx ON ai_chat_memory(conversation_id, "timestamp" DESC);

--changeset leijendary:create-vector-extension
CREATE EXTENSION IF NOT EXISTS vector;

--changeset leijendary:create-hstore-extension
CREATE EXTENSION IF NOT EXISTS hstore;

--changeset leijendary:create-uuid-ossp-extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--changeset leijendary:create-vector-store-table
CREATE TABLE vector_store (
    id uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    content text,
    metadata json,
    embedding vector(1536)
);

--changeset leijendary:create-vector-store-embedding-index
CREATE INDEX vector_store_embedding_idx ON vector_store USING HNSW (embedding vector_cosine_ops);
