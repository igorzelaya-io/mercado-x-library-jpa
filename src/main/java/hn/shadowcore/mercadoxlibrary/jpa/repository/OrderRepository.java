package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.core.Order;
import hn.shadowcore.mercadoxlibrary.jpa.repository.custom.CustomOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends BaseRepository<Order, String>, CustomOrderRepository {

    Optional<Order> findByUser_Id(UUID userId);

}
