CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE library.author
(
    id          UUID                        NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    first_name  VARCHAR(255)                NOT NULL,
    middle_name VARCHAR(255),
    last_name   VARCHAR(255)                NOT NULL,
    version     BIGINT                      NOT NULL DEFAULT 1,
    CONSTRAINT pk_author PRIMARY KEY (id)
);

CREATE TABLE library.book
(
    id         UUID                        NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    name       TEXT                        NOT NULL,
    isbn       CHAR(17)                    NOT NULL,
    cycle_id   UUID,
    series_id  UUID,
    version    BIGINT                      NOT NULL DEFAULT 1,
    CONSTRAINT pk_book PRIMARY KEY (id)
);

CREATE TABLE library.book_author
(
    author_id UUID NOT NULL,
    book_id   UUID NOT NULL,
    CONSTRAINT pk_book_author PRIMARY KEY (author_id, book_id)
);

CREATE TABLE library.cycle
(
    id         UUID                        NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    name       TEXT,
    version    BIGINT                      NOT NULL DEFAULT 1,
    CONSTRAINT pk_cycle PRIMARY KEY (id)
);

CREATE TABLE library.series
(
    id         UUID                        NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    name       TEXT                        NOT NULL,
    cycle_id   UUID,
    version    BIGINT                      NOT NULL DEFAULT 1,
    CONSTRAINT pk_series PRIMARY KEY (id)
);

ALTER TABLE library.author
    ADD CONSTRAINT author_full_name_uc UNIQUE (first_name, middle_name, last_name);

ALTER TABLE library.book_author
    ADD CONSTRAINT book_author_book_id_author_id_uc UNIQUE (book_id, author_id);

CREATE INDEX book_name_ix ON library.book (name);

ALTER TABLE library.book
    ADD CONSTRAINT book_cycle_id_fk FOREIGN KEY (cycle_id) REFERENCES library.cycle (id);

CREATE INDEX book_cycle_id_ix ON library.book (cycle_id);

ALTER TABLE library.book
    ADD CONSTRAINT book_series_id_fk FOREIGN KEY (series_id) REFERENCES library.series (id);

CREATE INDEX book_series_id_ix ON library.book (series_id);

ALTER TABLE library.series
    ADD CONSTRAINT SERIES_CYCLE_ID_FK FOREIGN KEY (cycle_id) REFERENCES library.cycle (id);

CREATE INDEX series_cycle_id_ix ON library.series (cycle_id);

ALTER TABLE library.book_author
    ADD CONSTRAINT book_author_book_id_fk FOREIGN KEY (book_id) REFERENCES library.book (id);

ALTER TABLE library.book_author
    ADD CONSTRAINT book_author_author_id_fk FOREIGN KEY (author_id) REFERENCES library.author (id);
