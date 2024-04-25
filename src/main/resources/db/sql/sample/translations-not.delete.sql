delete from sample_translation where id = :id and language <> all(:languages)
