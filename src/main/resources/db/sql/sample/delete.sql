update sample
set version = version + 1, deleted_by = ?, deleted_at = now()
where id = ? and version = ? and deleted_at is null
