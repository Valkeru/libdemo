CREATE TABLE library.book_instance
(
    id               uuid        NOT NULL DEFAULT uuidv7(),
    book_id          uuid        NOT NULL,
    inventory_number VARCHAR(25) NOT NULL,
    notes            jsonb       NOT NULL DEFAULT '[]',
    version          BIGINT      NOT NULL DEFAULT 0,
    created_at       timestamptz NOT NULL DEFAULT NOW(),
    updated_at       timestamptz,
    CONSTRAINT book_instance_pk PRIMARY KEY (id),
    CONSTRAINT book_instance_book_id_fk FOREIGN KEY (book_id) REFERENCES library.book (id),
    CONSTRAINT book_instance_inventory_number_uc UNIQUE (inventory_number)
);

CREATE INDEX IF NOT EXISTS book_instance_book_id_ix ON library.book_instance (book_id);
