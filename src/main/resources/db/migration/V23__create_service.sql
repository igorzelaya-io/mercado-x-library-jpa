CREATE TABLE core.service (
                              id UUID PRIMARY KEY,
                              name VARCHAR(255) NOT NULL,
                              description TEXT NOT NULL,
                              service_category_id UUID NOT NULL,
                              org_id UUID NOT NULL,
                              CONSTRAINT fk_service_category
                                  FOREIGN KEY (service_category_id)
                                      REFERENCES core.service_category(id),
                              CONSTRAINT fk_service_org
                                  FOREIGN KEY (org_id)
                                      REFERENCES auth.organization(id)
);
