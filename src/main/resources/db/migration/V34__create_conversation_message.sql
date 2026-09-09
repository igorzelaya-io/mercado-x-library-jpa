CREATE TABLE ai.conversation_message (
    id                    UUID        NOT NULL PRIMARY KEY,
    conversation_id       UUID        NOT NULL REFERENCES ai.conversation(id),
    role                  VARCHAR(16) NOT NULL,
    content               TEXT        NOT NULL,
    anthropic_message_id  VARCHAR(128),
    created_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_cmsg_conversation_id_created_at
    ON ai.conversation_message (conversation_id, created_at ASC);
