--liquibase formatted sql
--changeset leijendary:create-sample-table
CREATE TABLE sample (
    id character varying(28) PRIMARY KEY,
    name character varying(100) NOT NULL,
    description character varying(2000),
    amount numeric(12,2) NOT NULL,
    version smallint NOT NULL DEFAULT 0,
    created_at timestamp without time zone NOT NULL DEFAULT NOW(),
    created_by text NOT NULL,
    last_modified_at timestamp without time zone NOT NULL DEFAULT NOW(),
    last_modified_by text NOT NULL
);

--changeset leijendary:create-sample-name-lower-unique-index
CREATE UNIQUE index sample_name_key ON sample(LOWER(name));

--changeset leijendary:create-sample-created-at-id-index
CREATE INDEX sample_created_at_id_idx ON sample(created_at DESC, id);

--changeset leijendary:create-sample-translation-table
CREATE TABLE sample_translation (
    id character varying(28) REFERENCES sample(id) ON DELETE CASCADE,
    name character varying(100) NOT NULL,
    description character varying(200),
    language character varying(4),
    ordinal smallint NOT NULL,
    CONSTRAINT sample_translation_pkey PRIMARY KEY (id, language)
);

--changeset leijendary:create-sample-translation-id-index
CREATE INDEX sample_translation_id_idx ON sample_translation(id);

--changeset leijendary:create-image-table
CREATE TABLE image (
    id character varying(28) PRIMARY KEY,
    name character varying(250) NOT NULL unique,
    media_type character varying(15),
    validated boolean NOT NULL DEFAULT false,
    created_at timestamp without time zone NOT NULL DEFAULT NOW(),
    created_by text NOT NULL
);

--changeset leijendary:create-image-metadata-table
CREATE TABLE image_metadata (
    id character varying(28) REFERENCES image(id) ON DELETE CASCADE,
    name character varying(100) NOT NULL,
    value text NOT NULL,
    CONSTRAINT image_metadata_pkey PRIMARY KEY (id, name)
);

--changeset leijendary:create-image-metadata-id-index
CREATE INDEX image_metadata_id_idx ON image_metadata(id);

--changeset leijendary:create-sample-image-table
CREATE TABLE sample_image (
    id character varying(28) REFERENCES sample(id) ON DELETE CASCADE PRIMARY KEY,
    original character varying(250) NOT NULL REFERENCES image(name),
    preview character varying(250) NOT NULL REFERENCES image(name),
    thumbnail character varying(250) NOT NULL REFERENCES image(name)
);

--changeset leijendary:create-ai-chat-table
CREATE TABLE ai_chat (
    id character varying(28) PRIMARY KEY,
    title character varying(100) NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT NOW(),
    created_by text NOT NULL
);

--changeset leijendary:create-ai-chat-created-by-created-at-id-index
CREATE INDEX ai_chat_created_by_created_at_id_idx ON ai_chat(created_by, created_at DESC, id);

--changeset leijendary:create-ai-chat-memory-table
CREATE TABLE ai_chat_memory (
    session_id character varying(28) NOT NULL,
    content text NOT NULL,
    type character varying(10) NOT NULL,
    "timestamp" timestamp without time zone NOT NULL DEFAULT NOW()
);

--changeset leijendary:create-ai-chat-memory-session-id-timestamp-index
CREATE INDEX ai_chat_memory_session_id_timestamp_idx ON ai_chat_memory(session_id, "timestamp" DESC)
