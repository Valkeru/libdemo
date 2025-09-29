ALTER TABLE library.author
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE library.author
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE library.book
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE library.book
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE library.cycle
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE library.cycle
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE library.series
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE library.series
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;
