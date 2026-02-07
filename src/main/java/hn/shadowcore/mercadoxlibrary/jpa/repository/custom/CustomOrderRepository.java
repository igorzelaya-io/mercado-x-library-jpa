package hn.shadowcore.mercadoxlibrary.jpa.repository.custom;


import hn.shadowcore.mercadoxlibrary.entity.model.core.Order;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.OrderStatus;

import java.util.List;

public interface CustomOrderRepository {

    List<Order> findAllUnderReview();

    List<Order> findOrdersByUserAndStatus(String userId, OrderStatus orderStatus);

}
