package hn.shadowcore.mercadox.library.jpa.repository.custom.impl;

import hn.shadowcore.mercadox.library.jpa.predicate.UserPredicateFactory;
import hn.shadowcore.mercadox.library.jpa.repository.custom.CustomOrderRepository;
import hn.shadowcore.mercadox.library.jpa.querydsl.OrgAwareQueryFactory;
import hn.shadowcore.mercadox.library.entity.model.auth.QUser;
import hn.shadowcore.mercadox.library.entity.model.core.Order;
import hn.shadowcore.mercadox.library.entity.model.core.QOrder;
import hn.shadowcore.mercadox.library.entity.model.enums.OrderStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    private final OrgAwareQueryFactory jpaQueryFactory;

    private static final UserPredicateFactory userPredicateFactory = new UserPredicateFactory();

    private static final QOrder qOrder = QOrder.order;

    @Override
    public List<Order> findAllUnderReview() {
        return jpaQueryFactory.selectFrom(qOrder, qOrder.orgId)
                .where(qOrder.orderStatus.eq(OrderStatus.UNDER_REVIEW))
                .fetch().stream().toList();
    }

    @Override
    public List<Order> findOrdersByUserAndStatus(String userId, OrderStatus orderStatus) {
        QUser qUser = QUser.user;
        return jpaQueryFactory.selectFrom(qOrder, qOrder.organization().id)
                .join(qOrder.user(), qUser)
                .where(qOrder.orderStatus.eq(orderStatus)
                        .and(userPredicateFactory.equalsId(qUser, userId))
                        .and(userPredicateFactory.isActive(qUser)))
                .fetch().stream().toList();
    }

}
