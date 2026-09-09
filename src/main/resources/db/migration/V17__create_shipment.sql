CREATE TABLE core.shipment (
                               id UUID PRIMARY KEY,
                               location_id UUID,
                               placed_at TIMESTAMP,
                               delivered_at TIMESTAMP,
                               shipment_status VARCHAR(50),
                               customer_id UUID,
                               employee_id UUID,
                               org_id UUID,
                               CONSTRAINT fk_shipment_location
                                   FOREIGN KEY (location_id)
                                       REFERENCES core.location(id),
                               CONSTRAINT fk_shipment_customer
                                   FOREIGN KEY (customer_id)
                                       REFERENCES auth.users(id),
                               CONSTRAINT fk_shipment_employee
                                   FOREIGN KEY (employee_id)
                                       REFERENCES auth.users(id),
                               CONSTRAINT fk_shipment_org
                                   FOREIGN KEY (org_id)
                                       REFERENCES auth.organization(id)
);
