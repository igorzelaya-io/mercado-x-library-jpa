CREATE TABLE core.item (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           description TEXT NOT NULL,
                           unit_quantity INTEGER NOT NULL,
                           unit_price NUMERIC(12,2) NOT NULL,
                           in_stock BOOLEAN NOT NULL,
                           image VARCHAR(255),
                           org_id UUID,
                           category_id UUID,
                           inventory_id UUID,
                           shipping_id UUID,
                           CONSTRAINT fk_item_org
                               FOREIGN KEY (org_id)
                                   REFERENCES auth.organization(id),
                           CONSTRAINT fk_item_category
                               FOREIGN KEY (category_id)
                                   REFERENCES core.category(id),
                           CONSTRAINT fk_item_inventory
                               FOREIGN KEY (inventory_id)
                                   REFERENCES core.inventory(id),
                           CONSTRAINT fk_shipment_id_fk
                               FOREIGN KEY(shipping_id)
                                   REFERENCES core.shipment(id)
);
