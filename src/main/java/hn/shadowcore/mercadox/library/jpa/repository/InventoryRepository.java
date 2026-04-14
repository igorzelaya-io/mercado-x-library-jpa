package hn.shadowcore.mercadox.library.jpa.repository;


import hn.shadowcore.mercadox.library.entity.model.core.Inventory;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryRepository extends BaseRepository<Inventory, UUID> { }
