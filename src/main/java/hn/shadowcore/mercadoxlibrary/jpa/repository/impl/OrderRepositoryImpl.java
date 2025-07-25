package hn.shadowcore.mercadoxlibrary.jpa.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hn.shadowcore.mercadoxlibrary.entity.model.core.Order;
import hn.shadowcore.mercadoxlibrary.entity.model.core.QOrder;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.OrderStatus;
import hn.shadowcore.mercadoxlibrary.jpa.predicate.UserPredicateFactory;
import hn.shadowcore.mercadoxlibrary.jpa.querydsl.OrgAwareQueryFactory;
import hn.shadowcore.mercadoxlibrary.jpa.repository.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private OrgAwareQueryFactory jpaQueryFactory;

    private UserPredicateFactory userPredicateFactory;

    private static final QOrder qOrder = QOrder.order;

    @Override
    public List<Order> findAllUnderReview() {
        return jpaQueryFactory.selectFrom(qOrder, qOrder.orgId)
                .where(qOrder.orderStatus.eq(OrderStatus.UNDER_REVIEW))
                .fetchAll().stream().toList();
    }

    @Override
    public List<Order> findOrdersByUserAndStatus(String userId, OrderStatus orderStatus) {
        return jpaQueryFactory
                .selectFrom(qOrder, qOrder.orgId).where(qOrder.orderStatus.eq(orderStatus)
                        .and(userPredicateFactory.equalsId(userId))
                        .and(userPredicateFactory.isActive()))
                .fetchAll().stream().toList();
    }
}
