package hn.shadowcore.mercadox.library.jpa.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hn.shadowcore.mercadox.context.crypto.EncryptionAutoConfiguration;
import hn.shadowcore.mercadox.context.crypto.EnvVarMasterKeyService;
import hn.shadowcore.mercadox.context.crypto.MasterKeyProperties;
import hn.shadowcore.mercadox.library.entity.crypto.MasterKeyService;
import hn.shadowcore.mercadox.library.jpa.querydsl.OrgAwareQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@Profile("test")
@EnableAspectJAutoProxy
@ConditionalOnClass(EntityManager.class)
@Import({QueryDSLInfrastructureConfig.class, EncryptionAutoConfiguration.class})
public class H2JpaTestConfig {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // EncryptionAutoConfiguration (imported above) requires encryption.master-key.value.
    // Consumers that don't set that property (no application.yml/properties under
    // src/test/resources) never satisfy it, so this bean is the only MasterKeyService and backs
    // nothing off. Consumers that DO set the property (e.g. oauth's application-test.yml, needed
    // for their own encryption tests) satisfy EncryptionAutoConfiguration's condition, so its bean
    // registers too; @ConditionalOnMissingBean here makes this one defer to that bean instead of
    // colliding with it.
    @Bean
    @ConditionalOnMissingBean(MasterKeyService.class)
    public MasterKeyService masterKeyService() {
        byte[] key = new byte[32];
        SECURE_RANDOM.nextBytes(key);
        return new EnvVarMasterKeyService(new MasterKeyProperties(Base64.getEncoder().encodeToString(key)));
    }

    @Bean
    @Primary
    public DataSource dataSource() throws IOException {

        DataSource ds = DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:test-db-" + UUID.randomUUID() + ";MODE=PostgreSQL")
                .username("sa")
                .password("").build();

        // Only the versioned schema migrations run for tests. Seed data lives under
        // db/seed (loaded via Flyway's "local" profile in the services) and is
        // intentionally excluded here — integration tests build their own fixtures.
        Resource[] scripts = new PathMatchingResourcePatternResolver()
                .getResources("classpath:db/migration/*.sql");
        Arrays.sort(scripts, Comparator.comparingInt(r -> {
            String name = r.getFilename();
            return Integer.parseInt(name.substring(1, name.indexOf('_')));
        }));

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(scripts);
        populator.setContinueOnError(false);

        DatabasePopulatorUtils.execute(populator, ds);

        return ds;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            ConfigurableListableBeanFactory beanFactory
    ) {

        LocalContainerEntityManagerFactoryBean emf =
                new LocalContainerEntityManagerFactoryBean();

        emf.setDataSource(dataSource);
        emf.setPackagesToScan("hn.shadowcore.mercadox.library.entity");

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(adapter);

        Map<String, Object> props = new HashMap<>();

        props.put("hibernate.hbm2ddl.auto", "validate");

        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        props.put("hibernate.show_sql", true);
        props.put("hibernate.format_sql", true);

        // Spring Boot's own JpaBaseConfiguration wires this automatically; this config
        // builds the EntityManagerFactory by hand and bypasses that path, so without this,
        // Hibernate can't resolve @Component-based AttributeConverters (e.g.
        // EncryptedStringConverter) that need Spring to inject their dependencies.
        props.put("hibernate.resource.beans.container", new SpringBeanContainer(beanFactory));

        emf.setJpaPropertyMap(props);

        return emf;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    @Bean
    public OrgAwareQueryFactory orgAwareQueryFactory(JPAQueryFactory delegate) {
        return new OrgAwareQueryFactory(delegate);
    }
}
