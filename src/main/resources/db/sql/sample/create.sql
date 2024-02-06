insert into sample (name, description, amount, created_by, last_modified_by)
values (?, ?, ?, ?, ?)
returning id, name, description, amount, version, null translations, created_at
