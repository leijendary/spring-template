insert into sample_translation (id, name, description, language, ordinal)
select * from unnest(:ids::bigint[], :names::text[], :descriptions::text[], :languages::text[], :ordinals::smallint[])
on conflict (id, language) do update
set
    name = excluded.name,
    description = excluded.description,
    language = excluded.language,
    ordinal = excluded.ordinal
returning name, description, language, ordinal
