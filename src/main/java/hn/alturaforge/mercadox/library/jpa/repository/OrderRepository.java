package hn.alturaforge.mercadox.library.jpa.repository;

import hn.alturaforge.mercadox.library.jpa.repository.custom.CustomOrderRepository;
import hn.alturaforge.mercadox.library.entity.model.core.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends BaseRepository<Order, String>, CustomOrderRepository {

    Optional<Order> findByUser_Id(UUID userId);

}
