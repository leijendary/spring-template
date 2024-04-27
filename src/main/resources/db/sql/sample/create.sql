insert into sample (name, description, amount, created_by, last_modified_by)
values (:name, :description, :amount, :createdBy, :lastModifiedBy)
returning id, name, description, amount, version, created_at
