package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.core.Inventory;
import hn.shadowcore.mercadoxlibrary.entity.model.core.Item;

public interface CustomItemRepository {

    Item findItemWithInventory(String itemId);

    Inventory findItemInventory(String itemId);

}
