package hn.shadowcore.mercadox.library.jpa.repository.custom;


import hn.shadowcore.mercadox.library.entity.model.core.Order;
import hn.shadowcore.mercadox.library.entity.model.enums.OrderStatus;

import java.util.List;

public interface CustomOrderRepository {

    List<Order> findAllUnderReview();

    List<Order> findOrdersByUserAndStatus(String userId, OrderStatus orderStatus);

}
