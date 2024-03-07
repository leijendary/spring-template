update sample
set version = version + 1, deleted_by = :deletedBy, deleted_at = now()
where id = :id and version = :version and deleted_at is null
