update sample
set
    name = ?,
    description = ?,
    amount = ?,
    version = version + 1,
    last_modified_at = now(),
    last_modified_by = ?
where id = ? and version = ?
returning id, name, description, amount, version, null translations, created_at
