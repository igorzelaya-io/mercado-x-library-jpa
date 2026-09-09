ALTER TABLE ai.organization_whatsapp_config
    ADD COLUMN plan VARCHAR(20) NOT NULL DEFAULT 'STARTER';

ALTER TABLE ai.organization_whatsapp_config
    ADD COLUMN allow_overage BOOLEAN NOT NULL DEFAULT FALSE;