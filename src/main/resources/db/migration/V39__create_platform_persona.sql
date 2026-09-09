-- Platform-wide persona paragraphs — apply to every tenant, no organization_id
-- scoping. Composed ahead of an org's own organization_persona rows to form
-- the full Claude system prompt (platform BASE first, then org-specific rows).
CREATE TABLE ai.platform_persona (
    id           UUID        NOT NULL PRIMARY KEY,
    persona_key  VARCHAR(50) NOT NULL UNIQUE,
    prompt_text  TEXT        NOT NULL,
    active       BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
