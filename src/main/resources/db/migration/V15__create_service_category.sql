CREATE TABLE core.service_category (
                                       id UUID PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
                                       org_id UUID NOT NULL,
                                       CONSTRAINT fk_service_category_org
                                           FOREIGN KEY (org_id)
                                               REFERENCES auth.organization(id)
);
