-- =============================================================================
-- Production configuration — Altura Forge Software
--
-- Manual script — NOT managed by Flyway (no V__/R__ prefix, so Flyway never
-- scans or applies it). Run by hand against the target database, e.g.:
--   psql "$DATABASE_URL" -f this-file
-- All statements are idempotent (INSERT ... WHERE NOT EXISTS), so it is safe
-- to run repeatedly.
--
-- Scope: this organization and its owner user ALREADY EXIST (created via the
-- normal signup flow) — this script only adds the AI/notification
-- configuration for that org. It intentionally does NOT touch
-- auth.organization / auth.user_type / auth.users.
--
-- Target org: cc8af091-b512-4ed9-a11e-304529bb1dd0
--
-- Template key convention: {ORG_PREFIX}_{TEMPLATE_TYPE}
--   ALTURA_LEAD_CREATION_TEMPLATE  — initial greeting on lead creation
--   ALTURA_REENGAGEMENT_TEMPLATE   — 24-hour window fallback
--
-- Placeholders that MUST be replaced with real Meta values before going live:
--   ALTURA_PHONE_NUMBER_ID         — Meta phone_number_id from WhatsApp Business Manager
--   ALTURA_WABA_ID                 — WhatsApp Business Account ID
--   REPLACE_WITH_META_ACCESS_TOKEN — permanent system user token from Meta
--
-- WhatsApp template names below must be created and approved in Meta Business
-- Manager before the template pipeline can fire:
--   altura_lead_welcome    — maps to ALTURA_LEAD_CREATION_TEMPLATE
--   altura_reengagement    — maps to ALTURA_REENGAGEMENT_TEMPLATE
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 1. WhatsApp configuration
-- ---------------------------------------------------------------------------
INSERT INTO ai.organization_whatsapp_config (
    id, organization_id, phone_number_id, waba_id,
    ai_enabled, access_token, default_reengagement_template
)
SELECT 'a0000000-0000-0000-0000-000000000001',
       'cc8af091-b512-4ed9-a11e-304529bb1dd0',
       'ALTURA_PHONE_NUMBER_ID',
       'ALTURA_WABA_ID',
       true,
       'REPLACE_WITH_META_ACCESS_TOKEN',
       'ALTURA_REENGAGEMENT_TEMPLATE'
WHERE NOT EXISTS (
    SELECT 1 FROM ai.organization_whatsapp_config
    WHERE organization_id = 'cc8af091-b512-4ed9-a11e-304529bb1dd0'
);

-- ---------------------------------------------------------------------------
-- 2. Notification template — lead welcome (fires on LEAD_CREATED event)
--
-- Meta-approved template text (register as "altura_lead_welcome"):
--   "Hi {{1}}! 👋 Welcome to Altura Forge Software. I'm your AI assistant,
--    here to help {{2}} explore what we can build together. What's on your mind?"
--   {{1}} = userName  (variable_order 1)
--   {{2}} = orgName   (variable_order 2)
-- ---------------------------------------------------------------------------
INSERT INTO core.notification_template (
    template_key, channel, language_code, org_id,
    subject, whatsapp_template_name, body_html,
    active, system_template, created_at, updated_at
)
SELECT 'ALTURA_LEAD_CREATION_TEMPLATE',
       'WHATSAPP',
       'en',
       'cc8af091-b512-4ed9-a11e-304529bb1dd0',
       NULL,
       'altura_lead_welcome',
       NULL,
       true,
       true,
       NOW(),
       NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM core.notification_template
    WHERE org_id = 'cc8af091-b512-4ed9-a11e-304529bb1dd0'
    AND   template_key = 'ALTURA_LEAD_CREATION_TEMPLATE'
    AND   channel = 'WHATSAPP'
    AND   language_code = 'en'
);

INSERT INTO core.notification_template_variables (notification_template_id, variable, variable_order)
SELECT t.id, 'userName', 1
FROM   core.notification_template t
WHERE  t.template_key = 'ALTURA_LEAD_CREATION_TEMPLATE'
AND    t.org_id       = 'cc8af091-b512-4ed9-a11e-304529bb1dd0'
AND    NOT EXISTS (
    SELECT 1 FROM core.notification_template_variables v
    WHERE v.notification_template_id = t.id
    AND   v.variable_order = 1
);

INSERT INTO core.notification_template_variables (notification_template_id, variable, variable_order)
SELECT t.id, 'orgName', 2
FROM   core.notification_template t
WHERE  t.template_key = 'ALTURA_LEAD_CREATION_TEMPLATE'
AND    t.org_id       = 'cc8af091-b512-4ed9-a11e-304529bb1dd0'
AND    NOT EXISTS (
    SELECT 1 FROM core.notification_template_variables v
    WHERE v.notification_template_id = t.id
    AND   v.variable_order = 2
);

-- ---------------------------------------------------------------------------
-- 3. Notification template — reengagement (fires when 24-hour window expires)
--
-- Meta-approved template text (register as "altura_reengagement"):
--   "Hi! 👋 It's been a little while since we last connected. The Altura Forge
--    team is still here whenever you're ready — just reply to this message and
--    we'll jump right back in. Whether you have a new idea or an open question,
--    we're excited to help you build something great."
-- No variables — static re-engagement message.
-- ---------------------------------------------------------------------------
INSERT INTO core.notification_template (
    template_key, channel, language_code, org_id,
    subject, whatsapp_template_name, body_html,
    active, system_template, created_at, updated_at
)
SELECT 'ALTURA_REENGAGEMENT_TEMPLATE',
       'WHATSAPP',
       'en',
       'cc8af091-b512-4ed9-a11e-304529bb1dd0',
       NULL,
       'altura_reengagement',
       NULL,
       true,
       true,
       NOW(),
       NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM core.notification_template
    WHERE org_id = 'cc8af091-b512-4ed9-a11e-304529bb1dd0'
    AND   template_key = 'ALTURA_REENGAGEMENT_TEMPLATE'
    AND   channel = 'WHATSAPP'
    AND   language_code = 'en'
);

-- ---------------------------------------------------------------------------
-- 4. Platform-wide BASE persona — shared across all tenants, not org-specific.
-- Same row/ID as the dev fixture in R__altura_forge_persona_content.sql;
-- idempotent via WHERE NOT EXISTS, so it is a no-op if already present.
-- ---------------------------------------------------------------------------
INSERT INTO ai.platform_persona (id, persona_key, prompt_text, active, created_at, updated_at)
SELECT
    '88888888-8888-8888-8888-888888888888',
    'BASE',
    'You are an AI assistant communicating with a customer over WhatsApp on behalf of the '
    || 'business you represent. Keep responses concise, warm, and conversational — this is a '
    || 'chat, not an email. Always be transparent that you are an AI assistant, not a human '
    || 'team member, if the customer asks or when it is natural to clarify early in a new '
    || 'conversation. If a request falls outside what you can confidently handle, or the '
    || 'customer asks to speak with a person, let them know a team member will follow up '
    || 'directly. Never make commitments on pricing, contracts, or delivery timelines — those '
    || 'require a human''s involvement. Stay strictly on topics related to the business you '
    || 'represent; if the conversation drifts elsewhere, gently steer it back.',
    true, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM ai.platform_persona WHERE persona_key = 'BASE'
);

-- ---------------------------------------------------------------------------
-- 5. Altura Forge Software — org-specific identity and task personas
-- ---------------------------------------------------------------------------
INSERT INTO ai.organization_persona (id, organization_id, persona_key, prompt_text, active, created_at, updated_at)
SELECT
    'a0000000-0000-0000-0000-000000000002',
    'cc8af091-b512-4ed9-a11e-304529bb1dd0',
    'COMPANY',
    'You represent Altura Forge Software, a custom software development company that builds '
    || 'tailored web apps, backend systems, and AI-powered tools for its clients. The company''s '
    || 'founder and primary contact is Igor Zelaya.',
    true, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM ai.organization_persona
    WHERE organization_id = 'cc8af091-b512-4ed9-a11e-304529bb1dd0'
    AND   persona_key = 'COMPANY'
);

INSERT INTO ai.organization_persona (id, organization_id, persona_key, prompt_text, active, created_at, updated_at)
SELECT
    'a0000000-0000-0000-0000-000000000003',
    'cc8af091-b512-4ed9-a11e-304529bb1dd0',
    'SALES',
    'When a customer is exploring what Altura Forge can build for them, focus on '
    || 'understanding their needs before pitching: ask about their business, the problem they '
    || 'are trying to solve, and their rough timeline or budget range if it comes up naturally. '
    || 'Highlight that Altura Forge builds custom software — web apps, backend systems, '
    || 'AI-powered tools — tailored to the client, not off-the-shelf products. Do not quote '
    || 'exact prices or sign anyone up; instead, once you have a clear picture of what they '
    || 'need, let them know a member of the team will follow up with a proposal or to schedule '
    || 'a call.',
    true, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM ai.organization_persona
    WHERE organization_id = 'cc8af091-b512-4ed9-a11e-304529bb1dd0'
    AND   persona_key = 'SALES'
);

INSERT INTO ai.organization_persona (id, organization_id, persona_key, prompt_text, active, created_at, updated_at)
SELECT
    'a0000000-0000-0000-0000-000000000004',
    'cc8af091-b512-4ed9-a11e-304529bb1dd0',
    'BOOKING',
    'When a customer wants to schedule a call or meeting with the Altura Forge team, collect '
    || 'their preferred days/times, timezone, and a one-line summary of what they would like to '
    || 'discuss. Let them know their request has been received and that the team will confirm '
    || 'the exact time shortly — do not confirm a specific slot yourself, since you do not have '
    || 'direct access to real-time calendar availability. Be efficient: do not ask more than two '
    || 'or three questions before wrapping up.',
    true, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM ai.organization_persona
    WHERE organization_id = 'cc8af091-b512-4ed9-a11e-304529bb1dd0'
    AND   persona_key = 'BOOKING'
);
