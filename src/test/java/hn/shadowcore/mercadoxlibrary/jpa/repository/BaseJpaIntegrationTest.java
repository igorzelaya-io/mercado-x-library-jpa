package hn.shadowcore.mercadoxlibrary.jpa.repository;

import com.redis.testcontainers.RedisContainer;
import hn.shadowcore.mercadoxcontext.config.RedisConfig;
import hn.shadowcore.mercadoxcontext.config.RedisTtlConfig;
import hn.shadowcore.mercadoxcontext.utils.OrgIdContextHolder;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.Organization;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.UserType;
import hn.shadowcore.mercadoxlibrary.entity.model.core.Order;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.OrderStatus;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.UserTypeName;
import hn.shadowcore.mercadoxlibrary.jpa.config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Transactional
@Testcontainers
@ComponentScan(basePackages = {
    "hn.shadowcore.mercadoxlibrary"
})
@Import({ RedisConfig.class, RedisTtlConfig.class, JpaConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseJpaIntegrationTest {

//    @Container
//    @SuppressWarnings("resource")
//    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>
//            (DockerImageName.parse("postgres:16-alpine"))
//            .withDatabaseName("mercado_x")
//            .withUsername("postgres")
//            .withPassword("");
//
    @Container
    protected static RedisContainer redis = new RedisContainer
            (DockerImageName.parse("redis:7-alpine"));

//    @DynamicPropertySource
//    static void bindProps(DynamicPropertyRegistry r) {
//        r.add("spring.datasource.url", () -> {
//            if (!postgres.isRunning()) postgres.start();
//            return postgres.getJdbcUrl();
//        });
//        r.add("spring.datasource.username", () -> {
//            if (!postgres.isRunning()) postgres.start();
//            return postgres.getUsername();
//        });
//        r.add("spring.datasource.password", () -> {
//            if (!postgres.isRunning()) postgres.start();
//            return postgres.getPassword();
//        });
//
//        r.add("spring.data.redis.host", () -> {
//            if (!redis.isRunning()) redis.start();
//            return redis.getHost();
//        });
//        r.add("spring.data.redis.port", () -> {
//            if (!redis.isRunning()) redis.start();
//            return redis.getMappedPort(6379);
//        });
//    }

    @DynamicPropertySource
    static void bindProperties(DynamicPropertyRegistry r) {

        r.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/mercado_x");
        r.add("spring.datasource.username", () -> "postgres");
        r.add("spring.datasource.password", () -> "");

        r.add("spring.data.redis.host", () -> {
            if (!redis.isRunning()) {
                redis.start();
            }
            return redis.getHost();
        });

        r.add("spring.data.redis.port", () -> {
            if (!redis.isRunning()) {
                redis.start();
            }
            return redis.getMappedPort(6379);
        });
    }

    @PersistenceContext
    protected EntityManager em;

    protected Organization organization;

    protected User user;

    protected Order order;

    @BeforeEach
    public void init() {

        em.createNativeQuery("DELETE FROM core.order").executeUpdate();
        em.createNativeQuery("DELETE FROM auth.user").executeUpdate();
        em.createNativeQuery("DELETE FROM auth.organization").executeUpdate();
        em.flush();
        em.clear();

        organization = Organization.buildStaticTestOrg();
        persistAll(organization);
        OrgIdContextHolder.setTenantId(organization.getId().toString());

        user = buildBaseUser();
        user.setOrganization(organization);
        persistAll(user);

        order = buildBaseOrder();
        order.setUser(user);
        order.setOrganization(organization);
        persistAll(order);

        em.clear();
    }

    @AfterEach
    public void clear() {
        OrgIdContextHolder.clear();
        em.clear();
    }

    @SafeVarargs
    protected final <T> void persistAll(T... entities) {
        for(T entity : entities) {
            em.persist(entity);
        }
        em.flush();
    }

    protected Organization buildBaseOrganization() {
        return Organization.builder()
                .name("Ex. org.")
                .enabled(true)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    protected User buildBaseUser() {
        return User.builder()
                .username("testtusername")
                .firstName("Testing").lastName("Davidson").email("a@example.com")
                .password("test").phoneNumber("0000-0000").enabled(true)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .userType(UserType.builder()
                        .name(UserTypeName.BUYER)
                        .build())
                .isAdmin(false)
                .organization(Organization.buildStaticTestOrg())
                .build();
    }

    protected Order buildBaseOrder() {
        return Order.builder()
                .id(Order.generateId()).orderStatus(OrderStatus.IN_PROGRESS)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .user(user)
                .organization(organization)
                .build();
    }

}
