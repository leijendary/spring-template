insert into sample_translation (id, name, description, language, ordinal)
select * from unnest(?::int[], ?::text[], ?::text[], ?::text[], ?::smallint[])
on conflict (id, language)
do update
set
    name = excluded.name,
    description = excluded.description,
    language = excluded.language,
    ordinal = excluded.ordinal
returning name, description, language, ordinal
