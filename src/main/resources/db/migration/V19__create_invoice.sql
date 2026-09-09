CREATE TABLE invoicing.invoice (
                                   id UUID PRIMARY KEY,
                                   total_price NUMERIC(14,2) NOT NULL,
                                   subtotal NUMERIC(14,2) NOT NULL,
                                   discount NUMERIC(14,2),
                                   invoice_date TIMESTAMP NOT NULL,
                                   void_due_date TIMESTAMP,
                                   invoice_number VARCHAR(255),
                                   invoice_url VARCHAR(500),
                                   tax1 NUMERIC(14,2) NOT NULL,
                                   tax2 NUMERIC(14,2),
                                   tax3 NUMERIC(14,2),
                                   user_id UUID,
                                   org_id UUID,
                                   shipping_id UUID,
                                   order_id VARCHAR(64),
                                   CONSTRAINT fk_invoice_user
                                       FOREIGN KEY (user_id)
                                           REFERENCES auth.users(id),
                                   CONSTRAINT fk_invoice_org
                                       FOREIGN KEY (org_id)
                                           REFERENCES auth.organization(id),
                                   CONSTRAINT fk_invoice_shipping
                                       FOREIGN KEY (shipping_id)
                                           REFERENCES core.shipment(id),
                                   CONSTRAINT fk_invoice_order
                                       FOREIGN KEY (order_id)
                                           REFERENCES core.orders(id)
);
