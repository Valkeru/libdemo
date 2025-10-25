CREATE TABLE public.user_token
(
    id                   UUID                               NOT NULL,
    jwt                  TEXT                               NOT NULL,
    refresh_token        CHAR(32)                           NOT NULL,
    refresh_token_expiry TIMESTAMP WITHOUT TIME ZONE        NOT NULL,
    user_id              UUID REFERENCES library.users (id) NOT NULL,
    CONSTRAINT pk_user_token PRIMARY KEY (id),
    CONSTRAINT refresh_token_ui UNIQUE (refresh_token)
);

CREATE INDEX refresh_token_ix ON public.user_token (refresh_token);
