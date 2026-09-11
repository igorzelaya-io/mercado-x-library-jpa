package hn.alturaforge.mercadox.library.jpa.repository;


import hn.alturaforge.mercadox.library.entity.model.core.Inventory;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryRepository extends BaseRepository<Inventory, UUID> { }
