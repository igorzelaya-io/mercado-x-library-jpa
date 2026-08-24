-- =============================================================================
-- Seed data — Altura Forge Software
--
-- Manual dev-only seed script — NOT managed by Flyway. Run it by hand against
-- your local database whenever you want fixture data, e.g.:
--   docker exec -i mercadox-postgres psql -U postgres -d mercado_x < this-file
-- (or open it in DBeaver and execute). All statements are idempotent
-- (INSERT ... WHERE NOT EXISTS), so it is safe to run repeatedly. Flyway manages
-- schema/tables only and never scans this file, so it stays out of the schema
-- version line and out of prod.
--
-- Fixed UUIDs (dev-stable, easy to reference in tests and logs):
--   Org            11111111-1111-1111-1111-111111111111
--   User type      22222222-2222-2222-2222-222222222222
--   User (Igor)    33333333-3333-3333-3333-333333333333
--   WA config      44444444-4444-4444-4444-444444444444
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
--
-- Dev password hash is bcrypt('secret', 10). Change before any real deployment.
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 1. User type
-- ---------------------------------------------------------------------------
INSERT INTO auth.user_type (id, name, description)
SELECT '22222222-2222-2222-2222-222222222222',
       'OWNER',
       'Organization owner with full administrative access'
WHERE NOT EXISTS (
    SELECT 1 FROM auth.user_type WHERE id = '22222222-2222-2222-2222-222222222222'
);

-- ---------------------------------------------------------------------------
-- 2. Organization
-- ---------------------------------------------------------------------------
INSERT INTO auth.organization (id, name, enabled, created_at, org_admin_id)
SELECT '11111111-1111-1111-1111-111111111111',
       'Altura Forge Software',
       true,
       NOW(),
       NULL
WHERE NOT EXISTS (
    SELECT 1 FROM auth.organization WHERE id = '11111111-1111-1111-1111-111111111111'
);

-- ---------------------------------------------------------------------------
-- 3. Owner user — Igor Zelaya
-- Password: bcrypt('secret', 10) — dev only, rotate before production
-- ---------------------------------------------------------------------------
INSERT INTO auth.users (
    id, username, password, first_name, last_name,
    email, phone_number, enabled, is_admin,
    created_at, user_type_id, org_id
)
SELECT '33333333-3333-3333-3333-333333333333',
       'igor.zelaya',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'Igor',
       'Zelaya',
       'izelaya22@gmail.com',
       '+50494700005',
       true,
       true,
       NOW(),
       '22222222-2222-2222-2222-222222222222',
       '11111111-1111-1111-1111-111111111111'
WHERE NOT EXISTS (
    SELECT 1 FROM auth.users WHERE id = '33333333-3333-3333-3333-333333333333'
);

-- Wire admin back to org
UPDATE auth.organization
SET    org_admin_id = '33333333-3333-3333-3333-333333333333'
WHERE  id           = '11111111-1111-1111-1111-111111111111'
AND    org_admin_id IS NULL;

-- ---------------------------------------------------------------------------
-- 4. WhatsApp configuration
-- ---------------------------------------------------------------------------
INSERT INTO ai.organization_whatsapp_config (
    id, organization_id, phone_number_id, waba_id,
    ai_enabled, access_token, default_reengagement_template
)
SELECT '44444444-4444-4444-4444-444444444444',
       '11111111-1111-1111-1111-111111111111',
       'ALTURA_PHONE_NUMBER_ID',
       'ALTURA_WABA_ID',
       true,
       'REPLACE_WITH_META_ACCESS_TOKEN',
       'ALTURA_REENGAGEMENT_TEMPLATE'
WHERE NOT EXISTS (
    SELECT 1 FROM ai.organization_whatsapp_config WHERE id = '44444444-4444-4444-4444-444444444444'
);

-- ---------------------------------------------------------------------------
-- 5. Notification template — lead welcome (fires on LEAD_CREATED event)
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
       '11111111-1111-1111-1111-111111111111',
       NULL,
       'altura_lead_welcome',
       NULL,
       true,
       true,
       NOW(),
       NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM core.notification_template
    WHERE org_id = '11111111-1111-1111-1111-111111111111'
    AND   template_key = 'ALTURA_LEAD_CREATION_TEMPLATE'
    AND   channel = 'WHATSAPP'
    AND   language_code = 'en'
);

INSERT INTO core.notification_template_variables (notification_template_id, variable, variable_order)
SELECT t.id, 'userName', 1
FROM   core.notification_template t
WHERE  t.template_key = 'ALTURA_LEAD_CREATION_TEMPLATE'
AND    t.org_id       = '11111111-1111-1111-1111-111111111111'
AND    NOT EXISTS (
    SELECT 1 FROM core.notification_template_variables v
    WHERE v.notification_template_id = t.id
    AND   v.variable_order = 1
);

INSERT INTO core.notification_template_variables (notification_template_id, variable, variable_order)
SELECT t.id, 'orgName', 2
FROM   core.notification_template t
WHERE  t.template_key = 'ALTURA_LEAD_CREATION_TEMPLATE'
AND    t.org_id       = '11111111-1111-1111-1111-111111111111'
AND    NOT EXISTS (
    SELECT 1 FROM core.notification_template_variables v
    WHERE v.notification_template_id = t.id
    AND   v.variable_order = 2
);

-- ---------------------------------------------------------------------------
-- 6. Notification template — reengagement (fires when 24-hour window expires)
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
       '11111111-1111-1111-1111-111111111111',
       NULL,
       'altura_reengagement',
       NULL,
       true,
       true,
       NOW(),
       NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM core.notification_template
    WHERE org_id = '11111111-1111-1111-1111-111111111111'
    AND   template_key = 'ALTURA_REENGAGEMENT_TEMPLATE'
    AND   channel = 'WHATSAPP'
    AND   language_code = 'en'
);
