select id, name, description, amount, created_at
from sample
where deleted_at is null and name ilike concat('%%', :query::text, '%%')
order by created_at desc
limit :limit
offset :offset
