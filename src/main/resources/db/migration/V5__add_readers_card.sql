CREATE SCHEMA IF NOT EXISTS counter;
CREATE TABLE IF NOT EXISTS counter.library_card_number
(
    year  BIGINT PRIMARY KEY NOT NULL,
    value BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE library.library_card
(
    id                UUID                        NOT NULL DEFAULT uuidv7(),
    user_id           UUID                        NOT NULL REFERENCES library.user (id),
    number            BIGINT                      NOT NULL UNIQUE,
    is_active         BOOLEAN                     NOT NULL DEFAULT TRUE,
    inactivity_reason VARCHAR(50),
    valid_from        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    valid_to          TIMESTAMP WITH TIME ZONE,
    version           BIGINT                      NOT NULL DEFAULT 0,
    created_at        TIMESTAMP WITH TIME ZONE    NOT NULL DEFAULT now(),
    updated_at        TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_library_card PRIMARY KEY (id)
);

CREATE INDEX library_card_user_id ON library.library_card (user_id);

CREATE UNIQUE INDEX library_card_active_per_user_ui ON library.library_card (user_id, is_active) WHERE is_active;
