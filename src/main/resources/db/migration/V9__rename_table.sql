ALTER TABLE library.book_copy
    RENAME TO book_instance;

ALTER TABLE library.book_instance
    ADD COLUMN notes jsonb NOT NULL DEFAULT '[]';
ALTER TABLE library.book_instance
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
