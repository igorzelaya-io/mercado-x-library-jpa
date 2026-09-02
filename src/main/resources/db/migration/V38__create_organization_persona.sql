CREATE TABLE ai.organization_persona (
    id               UUID        NOT NULL PRIMARY KEY,
    organization_id  UUID        NOT NULL,
    persona_key      VARCHAR(50) NOT NULL,
    prompt_text      TEXT        NOT NULL,
    active           BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_org_persona_key UNIQUE (organization_id, persona_key)
);

CREATE INDEX idx_org_persona_org_id ON ai.organization_persona (organization_id);
