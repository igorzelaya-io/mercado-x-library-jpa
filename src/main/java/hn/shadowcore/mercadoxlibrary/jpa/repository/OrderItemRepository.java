package hn.shadowcore.mercadoxlibrary.jpa.repository;


import hn.shadowcore.mercadoxlibrary.entity.model.core.OrderItem;
import hn.shadowcore.mercadoxlibrary.entity.model.core.OrderItemsKey;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends BaseRepository<OrderItem, OrderItemsKey> {




}
