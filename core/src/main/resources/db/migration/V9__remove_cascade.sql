ALTER TABLE library.book_author
    DROP CONSTRAINT author_id_fk;

ALTER TABLE library.book_author
    ADD CONSTRAINT author_id_fk
        FOREIGN KEY (author_id) REFERENCES library.author (id);

ALTER TABLE library.book_author
    DROP CONSTRAINT book_id_fk;

ALTER TABLE library.book_author
    ADD CONSTRAINT book_id_fk FOREIGN KEY (book_id) REFERENCES library.book (id);
