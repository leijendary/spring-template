insert into sample_image (id, original_id, preview_id, thumbnail_id)
values (:id, :originalId, :previewId, :thumbnailId)
on conflict (id) do update
set
    original_id = excluded.original_id,
    preview_id = excluded.preview_id,
    thumbnail_id = excluded.thumbnail_id
