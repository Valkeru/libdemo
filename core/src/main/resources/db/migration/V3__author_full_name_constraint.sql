ALTER TABLE library.author
    ADD CONSTRAINT author_full_name_uc UNIQUE (first_name, middle_name, last_name);
