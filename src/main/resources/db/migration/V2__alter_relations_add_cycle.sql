DROP TABLE library.author_series CASCADE;

CREATE SEQUENCE IF NOT EXISTS library.cycle_id_seq START WITH 1 INCREMENT BY 5;

CREATE TABLE library.cycle
(
    id   BIGINT NOT NULL DEFAULT nextval('library.cycle_id_seq'),
    name TEXT,
    CONSTRAINT pk_cycle PRIMARY KEY (id)
);

ALTER TABLE library.book
    ADD cycle_id BIGINT;

ALTER TABLE library.series
    ADD cycle_id BIGINT;

ALTER TABLE library.book
    ADD CONSTRAINT book_cycle_id_fk FOREIGN KEY (cycle_id) REFERENCES library.cycle (id);

ALTER TABLE library.series
    ADD CONSTRAINT series_cycle_id_fk FOREIGN KEY (cycle_id) REFERENCES library.cycle (id);

ALTER TABLE library.author
    ALTER COLUMN first_name TYPE VARCHAR(255) USING (first_name::VARCHAR(255));

UPDATE library.author
SET first_name = '<Unknown>'
WHERE first_name IS NULL;
ALTER TABLE library.author
    ALTER COLUMN first_name SET NOT NULL;

ALTER TABLE library.author
    ALTER COLUMN last_name TYPE VARCHAR(255) USING (last_name::VARCHAR(255));

UPDATE library.author
SET last_name = '<Unknown>'
WHERE last_name IS NULL;
ALTER TABLE library.author
    ALTER COLUMN last_name SET NOT NULL;

ALTER TABLE library.author
    ALTER COLUMN middle_name TYPE VARCHAR(255) USING (middle_name::VARCHAR(255));
