CREATE SEQUENCE IF NOT EXISTS library.author_id_seq START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS library.book_id_seq START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS library.series_id_seq START WITH 1 INCREMENT BY 5;

CREATE TABLE library.author
(
    id          BIGINT NOT NULL DEFAULT nextval('library.author_id_seq'),
    first_name  TEXT,
    middle_name TEXT,
    last_name   TEXT,
    CONSTRAINT pk_author PRIMARY KEY (id)
);

CREATE TABLE library.book_author
(
    author_id BIGINT NOT NULL,
    book_id   BIGINT NOT NULL
);

CREATE TABLE library.author_series
(
    author_id BIGINT NOT NULL,
    series_id BIGINT NOT NULL
);

CREATE TABLE library.book
(
    id        BIGINT      NOT NULL DEFAULT nextval('library.book_id_seq'),
    name      TEXT        NOT NULL,
    isbn      VARCHAR(13) NOT NULL,
    series_id BIGINT,
    CONSTRAINT pk_book PRIMARY KEY (id)
);

CREATE TABLE library.series
(
    id   BIGINT NOT NULL DEFAULT nextval('library.series_id_seq'),
    name TEXT   NOT NULL,
    CONSTRAINT pk_series PRIMARY KEY (id)
);

ALTER TABLE library.book_author
    ADD CONSTRAINT book_id_author_id_uc UNIQUE (book_id, author_id);

ALTER TABLE library.author_series
    ADD CONSTRAINT author_id_series_id_ux UNIQUE (author_id, series_id);

CREATE INDEX book_name_ix ON library.book (name);

ALTER TABLE library.book
    ADD CONSTRAINT book_series_id_fk FOREIGN KEY (series_id) REFERENCES library.series (id);

ALTER TABLE library.book_author
    ADD CONSTRAINT book_id_fk FOREIGN KEY (book_id) REFERENCES library.book (id);

ALTER TABLE library.book_author
    ADD CONSTRAINT author_id_fk FOREIGN KEY (author_id) REFERENCES library.author (id);

ALTER TABLE library.author_series
    ADD CONSTRAINT series_id_fk FOREIGN KEY (series_id) REFERENCES library.series (id);

ALTER TABLE library.author_series
    ADD CONSTRAINT author_id_fk FOREIGN KEY (author_id) REFERENCES library.author (id);
