select count(*)
from sample
where deleted_at is null and name ilike concat('%%', ?::text, '%%')
