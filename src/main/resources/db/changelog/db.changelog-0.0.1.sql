--liquibase formatted sql
--changeset leijendary:create-sample-table
create table sample (
    id bigint generated always as identity primary key,
    name character varying(100) not null,
    description character varying(2000),
    amount numeric(12,2) not null,
    version smallint not null default 0,
    created_at timestamp with time zone not null default now(),
    created_by text not null,
    last_modified_at timestamp with time zone not null default now(),
    last_modified_by text not null,
    deleted_at timestamp with time zone,
    deleted_by text
);

--changeset leijendary:set-sample-id-initial-value
select setval('sample_id_seq', (select floor(random() * 19999 + 110000)::bigint));

--changeset leijendary:create-sample-name-lower-unique-index
create unique index sample_name_key on sample(lower(name)) where deleted_at is null;

--changeset leijendary:create-sample-created-at-id-index
create index sample_created_at_id_idx on sample(created_at desc, id desc);

--changeset leijendary:create-sample-translation-table
create table sample_translation (
    id bigint references sample(id),
    name character varying(100) not null,
    description character varying(200),
    language character varying(4),
    ordinal smallint not null,
    constraint sample_translation_pkey primary key (id, language)
);

--changeset leijendary:create-sample-translation-id-index
create index sample_translation_id_idx on sample_translation(id);
