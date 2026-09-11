-- =============================================================================
-- Altura Forge Software — tenant reference data (organization + AI personas)
--
-- Repeatable Flyway migration (R__): runs after all versioned migrations and
-- re-applies whenever this file's checksum changes. Every statement is
-- idempotent (INSERT ... WHERE NOT EXISTS on a natural key), so replays and
-- concurrent boots are safe.
--
-- PRODUCTION-READY: this is real data for MercadoX's first tenant, not a dev
-- fixture. It creates only:
--   * auth.organization              — the Altura Forge org row
--   * ai.platform_persona (BASE)     — platform-wide, tenant-independent
--   * ai.organization_persona x3     — Altura Forge COMPANY / SALES / BOOKING
-- All child-row ids are gen_random_uuid() (Postgres 13+ built-in). The
-- organization id is a FIXED constant — it is the identifier the demo/product
-- frontend passes to POST /api/v1/public/orgs/{orgId}/demo/chat and that
-- StandaloneDemoProperties.orgId defaults to. Do not regenerate it.
--
-- It deliberately does NOT touch ai.organization_whatsapp_config: that row
-- carries an envelope-encrypted access_token that a static migration cannot
-- produce. For the Cloud Run demo it is seeded by StandaloneDemoSeeder (Java,
-- @Profile "standalone"); in a full deployment it is created during real
-- WhatsApp onboarding.
--
-- LOCATION: db/seed/altura_forge/ — opt-in per service/environment via
-- spring.flyway.locations (mercado-x-ai's "standalone" profile adds it). The
-- sibling db/seed/R__altura_forge_seed_data.sql (dev fixture: bcrypt user,
-- placeholder Meta creds, notification templates) is NOT in this location.
--
-- Persona composition order at runtime: platform.BASE -> org.COMPANY ->
-- org.SALES -> org.BOOKING (platform rows first, then org rows by created_at).
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 1. Organization — Altura Forge Software
--    org_admin_id left NULL: no seeded user; assign via the normal flow later.
-- ---------------------------------------------------------------------------
INSERT INTO auth.organization (id, name, enabled, created_at, org_admin_id)
SELECT 'b4d1165d-72eb-4919-b7e1-fa293db233bb',
       'Altura Forge Software',
       true,
       NOW(),
       NULL
WHERE NOT EXISTS (
    SELECT 1 FROM auth.organization WHERE id = 'b4d1165d-72eb-4919-b7e1-fa293db233bb'
);

-- ---------------------------------------------------------------------------
-- 2. Platform-wide BASE persona — applies to every tenant, not just Altura.
--    Keyed by persona_key (UNIQUE); id generated on first insert.
-- ---------------------------------------------------------------------------
INSERT INTO ai.platform_persona (id, persona_key, prompt_text, active, created_at, updated_at)
SELECT
    gen_random_uuid(),
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
-- 3. Altura Forge Software — org-specific identity and task personas.
--    Keyed by (organization_id, persona_key) (UNIQUE); ids generated on first insert.
-- ---------------------------------------------------------------------------
INSERT INTO ai.organization_persona (id, organization_id, persona_key, prompt_text, active, created_at, updated_at)
SELECT
    gen_random_uuid(),
    'b4d1165d-72eb-4919-b7e1-fa293db233bb',
    'COMPANY',
    'You represent Altura Forge Software, a custom software development company that builds '
    || 'tailored web apps, backend systems, and AI-powered tools for its clients. The company''s '
    || 'founder and primary contact is Igor Zelaya.',
    true, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM ai.organization_persona
    WHERE organization_id = 'b4d1165d-72eb-4919-b7e1-fa293db233bb'
    AND   persona_key = 'COMPANY'
);

INSERT INTO ai.organization_persona (id, organization_id, persona_key, prompt_text, active, created_at, updated_at)
SELECT
    gen_random_uuid(),
    'b4d1165d-72eb-4919-b7e1-fa293db233bb',
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
    WHERE organization_id = 'b4d1165d-72eb-4919-b7e1-fa293db233bb'
    AND   persona_key = 'SALES'
);

INSERT INTO ai.organization_persona (id, organization_id, persona_key, prompt_text, active, created_at, updated_at)
SELECT
    gen_random_uuid(),
    'b4d1165d-72eb-4919-b7e1-fa293db233bb',
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
    WHERE organization_id = 'b4d1165d-72eb-4919-b7e1-fa293db233bb'
    AND   persona_key = 'BOOKING'
);
