CREATE SCHEMA IF NOT EXISTS security;

CREATE TABLE security.user_token
(
    id                   UUID                     NOT NULL DEFAULT uuidv4(),
    jwt                  TEXT                     NOT NULL,
    refresh_token        CHAR(32)                 NOT NULL UNIQUE,
    refresh_token_expiry TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id              UUID                     NOT NULL REFERENCES library.user (id),
    version              BIGINT                   NOT NULL DEFAULT 0,
    CONSTRAINT pk_user_token PRIMARY KEY (id)
);

CREATE INDEX refresh_token_ix ON security.user_token (refresh_token);

CREATE TABLE security.role_permission
(
    role       VARCHAR(50)  NOT NULL,
    permission VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role_permission PRIMARY KEY (role, permission)
);

INSERT INTO security.role_permission (role, permission)
VALUES ('MANAGER', 'AUTHOR_CREATE'),
       ('MANAGER', 'AUTHOR_UPDATE'),
       ('MANAGER', 'AUTHOR_DELETE'),
       ('MANAGER', 'CYCLE_CREATE'),
       ('MANAGER', 'CYCLE_UPDATE'),
       ('MANAGER', 'CYCLE_DELETE'),
       ('MANAGER', 'SERIES_CREATE'),
       ('MANAGER', 'SERIES_UPDATE'),
       ('MANAGER', 'SERIES_DELETE'),
       ('MANAGER', 'BOOK_CREATE'),
       ('MANAGER', 'BOOK_UPDATE'),
       ('MANAGER', 'BOOK_DELETE'),
       ('MANAGER', 'BOOK_INSTANCE_CREATE'),
       ('MANAGER', 'BOOK_INSTANCE_UPDATE'),
       ('MANAGER', 'BOOK_INSTANCE_VIEW'),
       ('MANAGER', 'BOOK_INSTANCE_DELETE'),
       ('MANAGER', 'SERVICE_LIBRARY_CARD_VIEW'),
       ('MANAGER', 'SERVICE_BOOK_LENDING_CANCEL'),
       ('MANAGER', 'SERVICE_BOOK_LENDING_RETURN'),
       ('LIBRARIAN', 'SERVICE_LIBRARY_CARD_CREATE'),
       ('LIBRARIAN', 'SERVICE_LIBRARY_CARD_VIEW'),
       ('LIBRARIAN', 'SERVICE_BOOK_LENDING_CANCEL'),
       ('LIBRARIAN', 'SERVICE_BOOK_LENDING_RETURN')
