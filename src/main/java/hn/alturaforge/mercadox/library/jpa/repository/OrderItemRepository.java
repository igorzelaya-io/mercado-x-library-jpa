package hn.alturaforge.mercadox.library.jpa.repository;


import hn.alturaforge.mercadox.library.entity.model.core.OrderItem;
import hn.alturaforge.mercadox.library.entity.model.core.OrderItemsKey;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends BaseRepository<OrderItem, OrderItemsKey> { }
