CREATE TABLE library.book_lending
(
    id                   UUID                     NOT NULL DEFAULT uuidv7(),
    book_instance_id     UUID                     NOT NULL REFERENCES library.book_instance (id),
    library_card_id      UUID                     NOT NULL REFERENCES library.library_card (id),
    reserved_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    reservation_due_date DATE,
    borrowed_at          TIMESTAMP WITH TIME ZONE,
    return_due_date      DATE,
    returned_at          TIMESTAMP WITH TIME ZONE,
    status               VARCHAR(50)              NOT NULL,
    version              BIGINT                   NOT NULL DEFAULT 0,
    created_at           TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP WITH TIME ZONE,
    CONSTRAINT book_lending_pk PRIMARY KEY (id),
    CONSTRAINT book_lending_status_chk CHECK ( status IN ('RESERVED', 'CANCELLED', 'BORROWED', 'RETURNED'))
);

CREATE INDEX book_lending_book_instance_ix ON library.book_lending (book_instance_id);
CREATE INDEX book_lending_library_card_ix ON library.book_lending (library_card_id);
CREATE UNIQUE INDEX book_lending_status_ui ON library.book_lending (book_instance_id) WHERE status NOT IN ('CANCELLED', 'RETURNED');
