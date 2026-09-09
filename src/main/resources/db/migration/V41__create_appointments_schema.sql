CREATE SCHEMA IF NOT EXISTS appointments;

CREATE TABLE appointments.bookable_service (
    id               UUID                     NOT NULL,
    org_id           UUID                     NOT NULL,
    name             VARCHAR(255)             NOT NULL,
    description      TEXT,
    duration_minutes INTEGER                  NOT NULL,
    active           BOOLEAN                  NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_bookable_service PRIMARY KEY (id),
    CONSTRAINT uq_bookable_service_org_id UNIQUE (org_id, id),
    CONSTRAINT ck_bookable_service_duration_positive CHECK (duration_minutes > 0)
);

CREATE INDEX ix_bookable_service_active
    ON appointments.bookable_service (org_id, active, name, id);

CREATE TABLE appointments.appointment_provider (
    id                  UUID                     NOT NULL,
    org_id              UUID                     NOT NULL,
    mercadox_user_id    UUID,
    display_name        VARCHAR(255)             NOT NULL,
    timezone            VARCHAR(64)              NOT NULL,
    google_calendar_id  VARCHAR(1024),
    active              BOOLEAN                  NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_appointment_provider PRIMARY KEY (id),
    CONSTRAINT uq_appointment_provider_org_id UNIQUE (org_id, id),
    CONSTRAINT uq_appointment_provider_org_user UNIQUE (org_id, mercadox_user_id)
);

CREATE INDEX ix_appointment_provider_active
    ON appointments.appointment_provider (org_id, active, display_name, id);

CREATE TABLE appointments.provider_offering (
    id                    UUID                     NOT NULL,
    org_id                UUID                     NOT NULL,
    provider_id           UUID                     NOT NULL,
    bookable_service_id   UUID                     NOT NULL,
    weekly_availability   JSONB                    NOT NULL DEFAULT '{}'::JSONB,
    active                BOOLEAN                  NOT NULL DEFAULT TRUE,
    created_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_provider_offering PRIMARY KEY (id),
    CONSTRAINT uq_provider_offering_org_id UNIQUE (org_id, id),
    CONSTRAINT uq_provider_offering_relationship
        UNIQUE (org_id, provider_id, bookable_service_id),
    CONSTRAINT fk_provider_offering_provider
        FOREIGN KEY (org_id, provider_id)
        REFERENCES appointments.appointment_provider (org_id, id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_provider_offering_service
        FOREIGN KEY (org_id, bookable_service_id)
        REFERENCES appointments.bookable_service (org_id, id)
        ON DELETE RESTRICT
);

CREATE INDEX ix_provider_offering_active_service
    ON appointments.provider_offering (org_id, active, bookable_service_id, provider_id);

CREATE TABLE appointments.appointment_booking (
    id                    UUID                     NOT NULL,
    org_id                UUID                     NOT NULL,
    provider_offering_id  UUID                     NOT NULL,
    customer_reference    VARCHAR(255)             NOT NULL,
    conversation_id       UUID,
    start_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    end_at                TIMESTAMP WITH TIME ZONE NOT NULL,
    timezone              VARCHAR(64)              NOT NULL,
    status                VARCHAR(16)              NOT NULL DEFAULT 'PENDING',
    google_calendar_id    VARCHAR(1024),
    google_event_id       VARCHAR(1024),
    idempotency_key       VARCHAR(255)             NOT NULL,
    created_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_appointment_booking PRIMARY KEY (id),
    CONSTRAINT uq_appointment_booking_idempotency UNIQUE (org_id, idempotency_key),
    CONSTRAINT fk_appointment_booking_offering
        FOREIGN KEY (org_id, provider_offering_id)
        REFERENCES appointments.provider_offering (org_id, id)
        ON DELETE RESTRICT,
    CONSTRAINT ck_appointment_booking_time_range CHECK (end_at > start_at),
    CONSTRAINT ck_appointment_booking_status
        CHECK (status IN ('PENDING', 'CONFIRMED', 'FAILED')),
    CONSTRAINT ck_appointment_booking_google_locator
        CHECK (google_event_id IS NULL OR google_calendar_id IS NOT NULL)
);

CREATE UNIQUE INDEX uq_appointment_booking_org_google_event
    ON appointments.appointment_booking (org_id, google_calendar_id, google_event_id);

CREATE INDEX ix_appointment_booking_offering_start
    ON appointments.appointment_booking (org_id, provider_offering_id, start_at);

CREATE INDEX ix_appointment_booking_upcoming
    ON appointments.appointment_booking (org_id, status, start_at);

CREATE INDEX ix_appointment_booking_customer_start
    ON appointments.appointment_booking (org_id, customer_reference, start_at DESC);

CREATE INDEX ix_appointment_booking_pending_reconcile
    ON appointments.appointment_booking (status, updated_at);
