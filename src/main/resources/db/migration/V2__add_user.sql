CREATE TYPE library.library_role AS ENUM ('ROLE_ADMIN', 'ROLE_LIBRARIAN', 'ROLE_USER', 'ROLE_READER');

CREATE TABLE library.user
(
    id       UUID         NOT NULL DEFAULT public.uuid_generate_v4(),
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     library.library_role  NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

-- Пароль для всех password
INSERT INTO library.user (username, password, role)
VALUES ('admin', '$2y$05$.Yg66ZXO4npyCuiw534xzeXxSiXougG.D0PnswdxhziXmi3Gxwmtu', 'ROLE_ADMIN'::library.library_role),
       ('default_librarian', '$2y$05$0y.icT8/gyXLMe2Mh0zRdebz6XyVa9npb0rtKL8Hqg3rsV9qcGnqS', 'ROLE_LIBRARIAN'::library.library_role),
       ('default_user', '$2y$05$uUzdAFPVOJyeCbCAUnE06.Fjt44kgvsNDqaKDnOstMEiaGIFSDySS', 'ROLE_USER'::library.library_role)
