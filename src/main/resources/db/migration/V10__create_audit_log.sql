CREATE SEQUENCE IF NOT EXISTS auth.audit_log_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE auth.audit_log (
                                id BIGINT DEFAULT nextval('auth.audit_log_seq') PRIMARY KEY,
                                user_id UUID,
                                org_id UUID,

                                action VARCHAR(255) NOT NULL,
                                timestamp TIMESTAMP NOT NULL,

                                CONSTRAINT fk_audit_log_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES auth.users(id),

                                CONSTRAINT fk_audit_log_org
                                    FOREIGN KEY (org_id)
                                        REFERENCES auth.organization(id)
);
