update sample
set
    name = :name,
    description = :description,
    amount = :amount,
    version = version + 1,
    last_modified_at = now(),
    last_modified_by = :lastModifiedBy
where id = :id and version = :version
returning id, name, description, amount, version, null translations, created_at
