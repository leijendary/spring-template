insert into sample_translation (id, name, description, language, ordinal)
select * from unnest(:ids::bigint[], :names::text[], :descriptions::text[], :languages::text[], :ordinals::smallint[])
returning name, description, language, ordinal
