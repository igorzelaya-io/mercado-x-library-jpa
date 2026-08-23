CREATE TABLE core.category (
                               id UUID PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               enabled BOOLEAN NOT NULL,
                               org_id UUID,
                               CONSTRAINT fk_category_org
                                   FOREIGN KEY (org_id)
                                       REFERENCES auth.organization(id)
);
