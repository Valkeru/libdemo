CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

ALTER TABLE library.book_author
    DROP CONSTRAINT author_id_fk;

ALTER TABLE library.author
    ALTER COLUMN id TYPE VARCHAR(100) USING id::VARCHAR(100);

ALTER TABLE library.book_author ALTER COLUMN author_id TYPE VARCHAR(100) USING author_id::VARCHAR(100);

ALTER TABLE library.book_author
    ADD CONSTRAINT author_id_fk
        FOREIGN KEY (author_id) REFERENCES library.author (id) ON UPDATE CASCADE;

ALTER TABLE library.author
    ALTER COLUMN id SET DEFAULT uuid_generate_v4();

UPDATE library.author SET id = uuid_generate_v4();

ALTER TABLE library.book_author
    DROP CONSTRAINT author_id_fk;

ALTER TABLE library.author
    ALTER COLUMN id TYPE uuid USING id::uuid;

ALTER TABLE library.book_author ALTER COLUMN author_id TYPE uuid USING author_id::uuid;

ALTER TABLE library.book_author
    ADD CONSTRAINT author_id_fk
        FOREIGN KEY (author_id) REFERENCES library.author (id) ON UPDATE CASCADE;
