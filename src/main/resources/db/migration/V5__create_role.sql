CREATE TABLE auth.role (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           org_id UUID,
                           CONSTRAINT fk_role_org
                               FOREIGN KEY (org_id)
                                   REFERENCES auth.organization(id)
);
