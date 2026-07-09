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
