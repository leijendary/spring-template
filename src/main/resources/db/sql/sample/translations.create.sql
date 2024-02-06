insert into sample_translation (id, name, description, language, ordinal)
select * from unnest(?::bigint[], ?::text[], ?::text[], ?::text[],  ?::smallint[])
returning name, description, language, ordinal
