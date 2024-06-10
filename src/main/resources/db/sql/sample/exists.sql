select exists(select 1 from sample where id = :id and deleted_at is null)
