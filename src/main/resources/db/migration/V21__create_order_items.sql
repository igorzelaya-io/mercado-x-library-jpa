CREATE TABLE core.order_items (
                                  item_id UUID NOT NULL,
                                  order_id VARCHAR(64) NOT NULL,
                                  quantity INTEGER,
                                  PRIMARY KEY (item_id, order_id),
                                  CONSTRAINT fk_order_item_item
                                      FOREIGN KEY (item_id)
                                          REFERENCES core.item(id),
                                  CONSTRAINT fk_order_item_order
                                      FOREIGN KEY (order_id)
                                          REFERENCES core.orders(id)
);
