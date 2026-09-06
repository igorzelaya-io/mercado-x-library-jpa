CREATE TABLE appointments.google_calendar_connection (
    id                          UUID                     NOT NULL,
    org_id                      UUID                     NOT NULL,
    google_subject              VARCHAR(255)             NOT NULL,
    google_account_email        VARCHAR(320)             NOT NULL,
    refresh_token_ciphertext    TEXT,
    credential_key_version      VARCHAR(255),
    granted_scopes              TEXT                     NOT NULL,
    status                      VARCHAR(24)              NOT NULL,
    created_by_user_email       VARCHAR(320)             NOT NULL,
    last_successful_refresh_at  TIMESTAMP WITH TIME ZONE,
    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_google_calendar_connection PRIMARY KEY (id),
    CONSTRAINT uq_google_calendar_connection_org UNIQUE (org_id),
    CONSTRAINT uq_google_calendar_connection_org_id UNIQUE (org_id, id),
    CONSTRAINT uq_google_calendar_connection_subject UNIQUE (org_id, google_subject),
    CONSTRAINT ck_google_calendar_connection_status
        CHECK (status IN ('ACTIVE', 'REAUTH_REQUIRED', 'REVOKED')),
    CONSTRAINT ck_google_calendar_connection_credential_state
        CHECK (
            (
                status = 'ACTIVE'
                AND refresh_token_ciphertext IS NOT NULL
                AND credential_key_version IS NOT NULL
            )
            OR
            (
                status IN ('REAUTH_REQUIRED', 'REVOKED')
                AND refresh_token_ciphertext IS NULL
                AND credential_key_version IS NULL
            )
        )
);

CREATE INDEX ix_google_calendar_connection_status
    ON appointments.google_calendar_connection (org_id, status);

ALTER TABLE appointments.appointment_provider
    ADD COLUMN google_calendar_connection_id UUID;

ALTER TABLE appointments.appointment_provider
    ADD CONSTRAINT fk_appointment_provider_google_connection
        FOREIGN KEY (org_id, google_calendar_connection_id)
        REFERENCES appointments.google_calendar_connection (org_id, id)
        ON DELETE RESTRICT;

CREATE UNIQUE INDEX uq_appointment_provider_google_calendar
    ON appointments.appointment_provider (
        org_id,
        google_calendar_connection_id,
        google_calendar_id
    );
