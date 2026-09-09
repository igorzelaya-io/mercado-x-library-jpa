CREATE TABLE invoicing.invoice_range (
                                         id UUID PRIMARY KEY,
                                         approved_range_prefix VARCHAR(50),
                                         range_due_date TIMESTAMP,
                                         range_start INTEGER NOT NULL,
                                         range_current INTEGER NOT NULL,
                                         range_end INTEGER NOT NULL,
                                         branch_id UUID,
                                         CONSTRAINT fk_invoice_range_branch
                                             FOREIGN KEY (branch_id)
                                                 REFERENCES auth.branch(id)
);
