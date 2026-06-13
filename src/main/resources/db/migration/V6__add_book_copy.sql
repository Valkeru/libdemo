CREATE TABLE library.book_copy
(
    id uuid NOT NULL DEFAULT public.uuid_generate_v4(),
    book_id uuid NOT null,
    inventory_number varchar(25),
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT book_copy_pk PRIMARY KEY (id),
    CONSTRAINT book_copy_book_id_fk FOREIGN KEY (book_id) REFERENCES library.book (id),
    CONSTRAINT book_copy_inventory_number_uc UNIQUE (inventory_number)
);

CREATE INDEX book_copy_book_id_ix ON library.book_copy (book_id);
