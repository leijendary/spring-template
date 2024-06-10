select o.name original, p.name preview, t.name thumbnail
from sample_image si
join image o
on o.id = si.original_id
join image p
on p.id = si.preview_id
join image t
on t.id = si.thumbnail_id
where si.id = :id
