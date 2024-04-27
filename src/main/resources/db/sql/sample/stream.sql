select id, name, description, amount, version, created_at
from sample
where deleted_at is null
