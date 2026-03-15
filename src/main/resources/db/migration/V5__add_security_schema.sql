CREATE SCHEMA IF NOT EXISTS security;

CREATE TABLE security.user_token
(
    id                   UUID                              NOT NULL,
    jwt                  TEXT                              NOT NULL,
    refresh_token        CHAR(32)                          NOT NULL,
    refresh_token_expiry TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
    user_id              UUID REFERENCES library.user (id) NOT NULL,
    version              BIGINT                            NOT NULL DEFAULT 1,
    CONSTRAINT pk_user_token PRIMARY KEY (id),
    CONSTRAINT refresh_token_ui UNIQUE (refresh_token)
);

INSERT INTO security.user_token
SELECT *
FROM public.user_token;

DROP TABLE public.user_token;

CREATE INDEX refresh_token_ix ON security.user_token (refresh_token);

CREATE TABLE security.role
(
    name VARCHAR(20) PRIMARY KEY NOT NULL
);
CREATE TABLE security.permissions
(
    id         UUID PRIMARY KEY DEFAULT public.uuid_generate_v4(),
    role_name  VARCHAR(20),
    permission VARCHAR(50),
    CONSTRAINT permission_role_fk FOREIGN KEY (role_name) REFERENCES security.role (name)
);
INSERT INTO security.role
VALUES ('ROLE_ADMIN'),
       ('ROLE_LIBRARIAN'),
       ('ROLE_USER');


ALTER TABLE library."user"
    ADD CONSTRAINT user_role_fk FOREIGN KEY (role) REFERENCES security.role (name);

