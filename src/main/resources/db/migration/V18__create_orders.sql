CREATE TABLE core.orders (
                              id VARCHAR(64) PRIMARY KEY,
                              order_status VARCHAR(50) NOT NULL,
                              created_at TIMESTAMP,
                              dispatched_by VARCHAR(255),
                              delivery_id VARCHAR(255),
                              user_id UUID,
                              shipment_id UUID,
                              org_id UUID,
                              CONSTRAINT fk_order_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES auth.users(id),
                              CONSTRAINT fk_order_shipment
                                  FOREIGN KEY (shipment_id)
                                      REFERENCES core.shipment(id),
                              CONSTRAINT fk_order_org
                                  FOREIGN KEY (org_id)
                                      REFERENCES auth.organization(id)
);
