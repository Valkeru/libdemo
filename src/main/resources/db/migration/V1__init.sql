CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA public;

--------------------------------------
--------- Author ---------------------
--------------------------------------

CREATE TABLE library.author
(
    id          UUID                     NOT NULL DEFAULT uuidv7(),
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    first_name  VARCHAR(255)             NOT NULL,
    middle_name VARCHAR(255),
    last_name   VARCHAR(255)             NOT NULL,
    version     BIGINT                   NOT NULL DEFAULT 0,
    CONSTRAINT pk_author PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS author_first_name_trgm ON library.author USING gin (first_name public.gin_trgm_ops);
CREATE INDEX IF NOT EXISTS author_middle_name_trgm ON library.author USING gin (middle_name public.gin_trgm_ops);
CREATE INDEX IF NOT EXISTS author_last_name_trgm ON library.author USING gin (last_name public.gin_trgm_ops);

--------------------------------------
--------- Cycle ----------------------
--------------------------------------

CREATE TABLE library.cycle
(
    id         UUID                     NOT NULL DEFAULT uuidv7(),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    name       TEXT,
    version    BIGINT                   NOT NULL DEFAULT 0,
    CONSTRAINT pk_cycle PRIMARY KEY (id)
);

--------------------------------------
--------- Series ---------------------
--------------------------------------

CREATE TABLE library.series
(
    id         UUID                     NOT NULL DEFAULT uuidv7(),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    name       TEXT                     NOT NULL,
    cycle_id   UUID,
    version    BIGINT                   NOT NULL DEFAULT 0,
    CONSTRAINT pk_series PRIMARY KEY (id),
    CONSTRAINT series_cycle_id_fk FOREIGN KEY (cycle_id) REFERENCES library.cycle (id)
);

CREATE INDEX IF NOT EXISTS series_cycle_id_ix ON library.series (cycle_id);

--------------------------------------
--------- Book -----------------------
--------------------------------------

CREATE TABLE library.book
(
    id         UUID                     NOT NULL DEFAULT uuidv7(),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    name       TEXT                     NOT NULL,
    isbn       CHAR(17)                 NOT NULL,
    cycle_id   UUID,
    series_id  UUID,
    version    BIGINT                   NOT NULL DEFAULT 0,
    CONSTRAINT pk_book PRIMARY KEY (id),
    CONSTRAINT book_cycle_id_fk FOREIGN KEY (cycle_id) REFERENCES library.cycle (id),
    CONSTRAINT book_series_id_fk FOREIGN KEY (series_id) REFERENCES library.series (id),
    CONSTRAINT book_isbn_uc UNIQUE (isbn)
);

CREATE INDEX IF NOT EXISTS book_cycle_id_ix ON library.book (cycle_id);
CREATE INDEX IF NOT EXISTS book_name_ix ON library.book (name);
CREATE INDEX IF NOT EXISTS book_series_id_ix ON library.book (series_id);

--------------------------------------
--------- Book - Author relation -----
--------------------------------------

CREATE TABLE library.book_author
(
    author_id UUID NOT NULL,
    book_id   UUID NOT NULL,
    CONSTRAINT pk_book_author PRIMARY KEY (book_id, author_id),
    CONSTRAINT book_author_book_id_fk FOREIGN KEY (book_id) REFERENCES library.book (id),
    CONSTRAINT book_author_author_id_fk FOREIGN KEY (author_id) REFERENCES library.author (id)
);

CREATE INDEX IF NOT EXISTS book_author_author_id_ix ON library.book_author (author_id);
