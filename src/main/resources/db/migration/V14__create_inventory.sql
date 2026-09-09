CREATE TABLE core.inventory (
                                id UUID PRIMARY KEY,
                                name VARCHAR(255),
                                quantity INTEGER,
                                org_id UUID NOT NULL,
                                branch_id UUID,
                                category_id UUID,
                                CONSTRAINT fk_inventory_org
                                    FOREIGN KEY (org_id)
                                        REFERENCES auth.organization(id),
                                CONSTRAINT fk_inventory_branch
                                    FOREIGN KEY (branch_id)
                                        REFERENCES auth.branch(id),
                                CONSTRAINT fk_inventory_category
                                    FOREIGN KEY (category_id)
                                        REFERENCES core.category(id)
);
