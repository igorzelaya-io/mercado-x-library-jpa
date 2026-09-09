CREATE TABLE invoicing.payment (
                                   id UUID PRIMARY KEY,
                                   payment_status VARCHAR(50) NOT NULL,
                                   org_id UUID NOT NULL,
                                   user_id UUID NOT NULL,
                                   item_id UUID,
                                   invoice_id UUID,
                                   CONSTRAINT fk_payment_org
                                       FOREIGN KEY (org_id)
                                           REFERENCES auth.organization(id),
                                   CONSTRAINT fk_payment_user
                                       FOREIGN KEY (user_id)
                                           REFERENCES auth.users(id),
                                   CONSTRAINT fk_payment_item
                                       FOREIGN KEY (item_id)
                                           REFERENCES core.item(id),
                                   CONSTRAINT fk_payment_invoice
                                       FOREIGN KEY (invoice_id)
                                           REFERENCES invoicing.invoice(id)
);
