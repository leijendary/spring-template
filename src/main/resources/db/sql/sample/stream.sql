select id, name, description, amount, version, null translations, created_at
from sample
where deleted_at is null
