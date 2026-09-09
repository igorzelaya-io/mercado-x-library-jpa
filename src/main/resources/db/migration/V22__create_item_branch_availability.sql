CREATE TABLE core.item_branch_availability (
                                               item_id UUID NOT NULL,
                                               branch_id UUID NOT NULL,
                                               PRIMARY KEY (item_id, branch_id),
                                               CONSTRAINT fk_item_branch_item
                                                   FOREIGN KEY (item_id)
                                                       REFERENCES core.item(id),
                                               CONSTRAINT fk_item_branch_branch
                                                   FOREIGN KEY (branch_id)
                                                       REFERENCES auth.branch(id)
);
