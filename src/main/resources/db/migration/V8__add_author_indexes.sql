CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA public;

CREATE INDEX IF NOT EXISTS author_first_name_trgm ON library.author USING gin (first_name gin_trgm_ops);
CREATE INDEX IF NOT EXISTS author_middle_name_trgm ON library.author USING gin (middle_name gin_trgm_ops);
CREATE INDEX IF NOT EXISTS author_last_name_trgm ON library.author USING gin (last_name gin_trgm_ops);
