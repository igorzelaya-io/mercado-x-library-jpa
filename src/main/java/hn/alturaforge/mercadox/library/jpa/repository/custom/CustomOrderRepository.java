package hn.alturaforge.mercadox.library.jpa.repository.custom;


import hn.alturaforge.mercadox.library.entity.model.core.Order;
import hn.alturaforge.mercadox.library.entity.model.enums.OrderStatus;

import java.util.List;

public interface CustomOrderRepository {

    List<Order> findAllUnderReview();

    List<Order> findOrdersByUserAndStatus(String userId, OrderStatus orderStatus);

}
