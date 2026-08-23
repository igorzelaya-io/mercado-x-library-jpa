CREATE TABLE ai.conversation (
    id                    UUID        NOT NULL PRIMARY KEY,
    org_id                UUID        NOT NULL,
    channel               VARCHAR(16) NOT NULL,
    external_contact_id   VARCHAR(64) NOT NULL,
    lead_id               UUID,
    status                VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    last_inbound_at       TIMESTAMP WITH TIME ZONE
);

CREATE UNIQUE INDEX idx_conv_org_channel_contact
    ON ai.conversation (org_id, channel, external_contact_id);

CREATE INDEX idx_conv_org_id ON ai.conversation (org_id);
