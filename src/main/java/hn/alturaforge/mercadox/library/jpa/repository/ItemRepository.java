package hn.alturaforge.mercadox.library.jpa.repository;

import hn.alturaforge.mercadox.library.jpa.repository.custom.CustomItemRepository;
import hn.alturaforge.mercadox.library.entity.model.core.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends BaseRepository<Item, UUID>, CustomItemRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Optional<Item> findByIdForUpdate(@Param("id") UUID id);

}
