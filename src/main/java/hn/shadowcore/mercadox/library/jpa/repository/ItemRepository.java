package hn.shadowcore.mercadox.library.jpa.repository;

import hn.shadowcore.mercadox.library.jpa.repository.custom.CustomItemRepository;
import hn.shadowcore.mercadox.library.entity.model.core.Item;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends BaseRepository<Item, UUID>, CustomItemRepository {



}
