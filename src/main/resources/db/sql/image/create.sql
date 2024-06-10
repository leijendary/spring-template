with inserted as (
    insert into image (name, created_by)
    values (:name, :createdBy)
    on conflict (lower(name)) do nothing
    returning id, name, validated
)
select * from inserted
union all
select id, name, validated
from image
where name = :name
limit 1
