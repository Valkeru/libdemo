CREATE TABLE library.user
(
    id       UUID         NOT NULL DEFAULT uuidv4(),
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

-- Password is "password" for all of entries
INSERT INTO library.user (username, password, role)
VALUES ('admin', '$2y$05$.Yg66ZXO4npyCuiw534xzeXxSiXougG.D0PnswdxhziXmi3Gxwmtu', 'ADMIN'),
       ('manager', '$2y$05$Onybp2.QtVdCcBA58OLHCusYaFBl8A8SnV7fSMridIhR5BctAX2RO', 'MANAGER'),
       ('default_librarian', '$2y$05$0y.icT8/gyXLMe2Mh0zRdebz6XyVa9npb0rtKL8Hqg3rsV9qcGnqS', 'LIBRARIAN'),
       ('default_user', '$2y$05$uUzdAFPVOJyeCbCAUnE06.Fjt44kgvsNDqaKDnOstMEiaGIFSDySS', 'USER');
