insert into image_metadata (id, name, value)
select :id, * from unnest(:names::text[], :values::text[])
on conflict (id, name) do nothing
