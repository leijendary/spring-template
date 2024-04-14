insert into sample_translation (id, name, description, language, ordinal)
select :id, * from unnest(:names::text[], :descriptions::text[], :languages::text[], :ordinals::smallint[])
returning name, description, language, ordinal
