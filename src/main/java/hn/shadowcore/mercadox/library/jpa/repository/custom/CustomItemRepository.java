package hn.shadowcore.mercadox.library.jpa.repository.custom;

import hn.shadowcore.mercadox.library.entity.model.core.Inventory;
import hn.shadowcore.mercadox.library.entity.model.core.Item;

public interface CustomItemRepository {

    Item findItemWithInventory(String itemId);

    Inventory findItemInventory(String itemId);

}
