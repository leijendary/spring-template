select s.id, t.name, t.description, amount, created_at
from sample s
left join lateral (
    select name, description
    from sample_translation
    where id = s.id
    order by (language = :language)::int desc, ordinal
    limit 1
) t on true
where
    deleted_at is null
    and (s.name ilike concat('%%', :query::text, '%%') or t.name ilike concat('%%', :query::text, '%%'))
    and (:createdAt::timestamptz is null or :id::bigint is null or (created_at, id) < (:createdAt, :id))
order by created_at desc, id desc
limit :limit
