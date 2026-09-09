-- Correlation ID spans the whole conversation (all inbound/outbound messages), not a
-- single message — persisted once on first inbound message, reused on every message
-- after. Nullable: existing conversations backfill lazily on their next inbound message.
ALTER TABLE ai.conversation ADD COLUMN correlation_id VARCHAR(64);
