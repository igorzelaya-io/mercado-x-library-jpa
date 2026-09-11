package hn.alturaforge.mercadox.library.jpa.repository.custom;

import hn.alturaforge.mercadox.library.entity.model.core.Inventory;
import hn.alturaforge.mercadox.library.entity.model.core.Item;

public interface CustomItemRepository {

    Item findItemWithInventory(String itemId);

    Inventory findItemInventory(String itemId);

}
