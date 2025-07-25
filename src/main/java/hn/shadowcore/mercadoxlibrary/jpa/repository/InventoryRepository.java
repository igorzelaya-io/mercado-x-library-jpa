package hn.shadowcore.mercadoxlibrary.jpa.repository;


import hn.shadowcore.mercadoxlibrary.entity.model.core.Inventory;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryRepository extends BaseRepository<Inventory, UUID> { }
