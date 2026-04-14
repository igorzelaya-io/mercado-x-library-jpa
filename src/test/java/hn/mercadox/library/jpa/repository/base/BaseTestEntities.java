package hn.mercadox.library.jpa.repository.base;


import hn.shadowcore.mercadox.library.entity.model.auth.Organization;
import hn.shadowcore.mercadox.library.entity.model.auth.User;
import hn.shadowcore.mercadox.library.entity.model.core.Order;
import hn.shadowcore.mercadox.library.entity.model.enums.OrderStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BaseTestEntities {

    protected Organization organization;

    protected Organization secondaryOrg;
    protected User user;
    protected Order order;

    protected Organization buildBaseOrganization() {
        return Organization.builder()
                .name("Test Org")
                .enabled(true)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    public static Organization buildSecondaryOrganization() {
        return Organization
                .builder()
                .name("Example Org.")
                .enabled(true)
                .createdAt(Timestamp.valueOf(LocalDateTime.now())).build();
    }

    protected User buildBaseUser() {
        return User.builder()
                .username("test-user")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .password("password")
                .enabled(true)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .isAdmin(false)
                .organization(organization)
                .build();
    }

    protected hn.shadowcore.mercadox.library.entity.model.core.Order buildBaseOrder() {
        return hn.shadowcore.mercadox.library.entity.model.core.Order.builder()
                .id(hn.shadowcore.mercadox.library.entity.model.core.Order.generateId())
                .orderStatus(OrderStatus.IN_PROGRESS)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .user(user)
                .organization(organization)
                .build();
    }
}
