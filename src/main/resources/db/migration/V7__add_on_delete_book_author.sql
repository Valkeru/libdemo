ALTER TABLE library.book_author
    DROP CONSTRAINT IF EXISTS author_id_fk;

ALTER TABLE library.book_author
    DROP CONSTRAINT IF EXISTS book_id_fk;

ALTER TABLE library.book_author
    ADD CONSTRAINT author_id_fk FOREIGN KEY (author_id) REFERENCES library.author (id) ON DELETE CASCADE;

ALTER TABLE library.book_author
    ADD CONSTRAINT book_id_fk FOREIGN KEY (book_id) REFERENCES library.book (id) ON DELETE CASCADE;
