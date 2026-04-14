package hn.shadowcore.mercadox.library.jpa.repository;

import hn.shadowcore.mercadox.library.jpa.repository.custom.CustomOrderRepository;
import hn.shadowcore.mercadox.library.entity.model.core.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends BaseRepository<Order, String>, CustomOrderRepository {

    Optional<Order> findByUser_Id(UUID userId);

}
