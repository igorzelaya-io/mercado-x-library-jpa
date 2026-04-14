package hn.shadowcore.mercadox.library.jpa.repository;


import hn.shadowcore.mercadox.library.entity.model.core.OrderItem;
import hn.shadowcore.mercadox.library.entity.model.core.OrderItemsKey;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends BaseRepository<OrderItem, OrderItemsKey> { }
