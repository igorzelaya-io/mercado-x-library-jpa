CREATE TABLE auth.organization (
                                   id UUID PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL,
                                   enabled BOOLEAN NOT NULL,
                                   created_at TIMESTAMP NOT NULL,
                                   org_admin_id VARCHAR(255)
);
