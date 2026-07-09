CREATE SCHEMA IF NOT EXISTS counter;
CREATE TABLE IF NOT EXISTS counter.library_card_number
(
    year  BIGINT PRIMARY KEY NOT NULL,
    value BIGINT             NOT NULL DEFAULT 1
);

CREATE TYPE library.library_card_inactivity_reason AS ENUM ('BANNED', 'EXPIRED');

CREATE TABLE library.library_card
(
    id                UUID                        NOT NULL DEFAULT public.uuid_generate_v4(),
    user_id           UUID                        NOT NULL REFERENCES library.user (id),
    number            BIGINT                      NOT NULL UNIQUE,
    is_active         BOOLEAN                     NOT NULL DEFAULT TRUE,
    inactivity_reason library.library_card_inactivity_reason,
    valid_from        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    valid_to          TIMESTAMP WITHOUT TIME ZONE,
    version           BIGINT                      NOT NULL DEFAULT 0,
    created_at        TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now(),
    updated_at        TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_library_card PRIMARY KEY (id)
);

CREATE INDEX library_card_user_id ON library.library_card (user_id);

CREATE UNIQUE INDEX library_card_active_per_user_ui ON library.library_card (user_id, is_active) WHERE is_active;
