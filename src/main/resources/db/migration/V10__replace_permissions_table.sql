DROP TABLE security.permissions;
DROP TABLE security.role;

CREATE TABLE security.role_permission
(
    role       library.library_role     NOT NULL,
    permission VARCHAR(255)             NOT NULL,
    version    BIGINT                   NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_role_permission PRIMARY KEY (role, permission)
);
