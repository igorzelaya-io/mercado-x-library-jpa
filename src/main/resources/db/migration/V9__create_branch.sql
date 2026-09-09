CREATE TABLE auth.branch (
                             id UUID PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             org_id UUID,
                             location_id UUID,
                             CONSTRAINT fk_branch_org
                                 FOREIGN KEY (org_id)
                                     REFERENCES auth.organization(id)
);

CREATE INDEX idx_branch_org_id ON auth.branch(org_id);
