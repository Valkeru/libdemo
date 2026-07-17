CREATE SCHEMA IF NOT EXISTS security;

CREATE TABLE security.user_token
(
    id                   UUID                              NOT NULL,
    jwt                  TEXT                              NOT NULL,
    refresh_token        CHAR(32)                          NOT NULL,
    refresh_token_expiry TIMESTAMP WITH TIME ZONE       NOT NULL,
    user_id              UUID REFERENCES library.user (id) NOT NULL,
    version              BIGINT                            NOT NULL DEFAULT 0,
    CONSTRAINT pk_user_token PRIMARY KEY (id),
    CONSTRAINT refresh_token_uc UNIQUE (refresh_token)
);

CREATE INDEX refresh_token_ix ON security.user_token (refresh_token);

CREATE TABLE security.role_permission
(
    role VARCHAR(50) NOT NULL,
    permission VARCHAR(255)             NOT NULL,
    CONSTRAINT pk_role_permission PRIMARY KEY (role, permission)
);
