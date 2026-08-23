CREATE TABLE ai.organization_whatsapp_config (
    id                              UUID        NOT NULL PRIMARY KEY,
    organization_id                 UUID        NOT NULL,
    phone_number_id                 VARCHAR(64) NOT NULL UNIQUE,
    waba_id                         VARCHAR(64) NOT NULL,
    ai_enabled                      BOOLEAN     NOT NULL DEFAULT FALSE,
    default_reengagement_template   VARCHAR(255)
);

CREATE INDEX idx_owc_organization_id ON ai.organization_whatsapp_config (organization_id);
