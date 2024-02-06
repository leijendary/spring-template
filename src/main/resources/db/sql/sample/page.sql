select id, name, description, amount, created_at
from sample
where deleted_at is null and name ilike concat('%%', ?::text, '%%')
order by created_at desc
limit ?
offset ?
