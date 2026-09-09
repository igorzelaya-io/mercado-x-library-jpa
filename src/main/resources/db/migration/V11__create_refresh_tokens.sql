CREATE SEQUENCE IF NOT EXISTS auth.refresh_token_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE auth.refresh_tokens (
                                     id BIGINT DEFAULT nextval('auth.refresh_token_seq') PRIMARY KEY,
                                     org_id UUID NOT NULL,
                                     user_id UUID NOT NULL,
                                     expires_at TIMESTAMP NOT NULL,

                                     CONSTRAINT fk_refresh_token_org
                                         FOREIGN KEY (org_id)
                                             REFERENCES auth.organization(id),

                                     CONSTRAINT fk_refresh_token_user
                                         FOREIGN KEY (user_id)
                                             REFERENCES auth.users(id)
);
