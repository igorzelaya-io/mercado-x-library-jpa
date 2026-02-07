package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;
import hn.shadowcore.mercadoxlibrary.entity.model.core.Order;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.OrderStatus;
import hn.shadowcore.mercadoxlibrary.jpa.config.JpaConfig;
import hn.shadowcore.mercadoxlibrary.jpa.querydsl.OrgAwareQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = { JpaConfig.class, OrgAwareQueryFactory.class, OrderRepository.class })
class OrderRepositoryIntTest extends BaseJpaIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldReturnByOrgIdFilter() {

        Optional<Order> retrievedOrder = orderRepository.findById(order.getId());

        assertThat(retrievedOrder).isPresent();
        assertThat(retrievedOrder.get().getId()).isEqualTo(order.getId());
        assertThat(retrievedOrder.get().getOrganization().getId()).isEqualTo(organization.getId());

    }

    @Test
    void shouldReturnOrdersUnderReview() {

        Order validOrder = Order.builder()
                .id(Order.generateId())
                .orderStatus(OrderStatus.UNDER_REVIEW)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .user(user).organization(organization)
                .build();

        persistAll(validOrder);

        List<Order> underReview = orderRepository.findAllUnderReview();

        assertThat(underReview).hasSize(1);
        assertThat(underReview.get(0).getId()).isEqualTo(validOrder.getId());
        assertThat(underReview.get(0).getOrderStatus()).isEqualTo(OrderStatus.UNDER_REVIEW);

    }

    @Test
    void shouldReturnByUserAndStatus() {

        User staticUser = User.buildStaticTestUser();
        staticUser.setOrganization(organization);
        persistAll(staticUser);

        Order validOrder = Order.builder()
                .id(Order.generateId())
                .orderStatus(OrderStatus.UNDER_REVIEW)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .organization(organization)
                .user(staticUser)
                .build();

        persistAll(validOrder);

        List<Order> orders = orderRepository
                .findOrdersByUserAndStatus(staticUser.getId().toString(), OrderStatus.UNDER_REVIEW);

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getUser().getId()).isEqualTo(staticUser.getId());

    }
}
