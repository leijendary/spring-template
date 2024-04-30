--liquibase formatted sql
--changeset leijendary:create-sample-table
CREATE TABLE sample (
    id bigint GENERATED ALWAYS AS IDENTITY (START 100000) PRIMARY KEY,
    name character varying(100) NOT NULL,
    description text,
    amount numeric(12,2) NOT NULL,
    version int NOT NULL DEFAULT 0,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    created_by text NOT NULL,
    last_modified_at timestamp with time zone NOT NULL DEFAULT now(),
    last_modified_by text NOT NULL,
    deleted_at timestamp with time zone,
    deleted_by text
);

--changeset leijendary:create-sample-name-lower-unique-index
CREATE UNIQUE INDEX sample_name_key ON sample(lower(name)) WHERE deleted_at IS NULL;

--changeset leijendary:create-sample-created-at-id-index
CREATE INDEX sample_created_at_id_idx ON sample(created_at DESC, id DESC);

--changeset leijendary:create-sample-translation-table
CREATE TABLE sample_translation (
    id bigint REFERENCES sample(id),
    name character varying(100) NOT NULL,
    description character varying(200),
    language character varying(4),
    ordinal smallint NOT NULL,
    PRIMARY KEY (id, language)
);
