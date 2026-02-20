package hn.shadowcore.mercadoxlibrary.jpa.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hn.shadowcore.mercadoxlibrary.jpa.querydsl.OrgAwareQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("test")
@EnableAspectJAutoProxy
@Import(QueryDSLInfrastructureConfig.class)
public class H2JpaTestConfig {

    @Bean
    @Primary
    public DataSource dataSource() {

        DataSource ds = DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:test-db;DB_CLOSE_DELAY=-1;MODE=PostgreSQL")
                .username("sa")
                .password("")
                .build();

        // Execute schema BEFORE Hibernate starts
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.setContinueOnError(false);

        DatabasePopulatorUtils.execute(populator, ds);

        return ds;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource
    ) {

        LocalContainerEntityManagerFactoryBean emf =
                new LocalContainerEntityManagerFactoryBean();

        emf.setDataSource(dataSource);
        emf.setPackagesToScan("hn.shadowcore.mercadoxlibrary.entity");

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(adapter);

        Map<String, Object> props = new HashMap<>();

        // 🔥 This is what you want
        props.put("hibernate.hbm2ddl.auto", "validate");

        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        // Optional but helpful
        props.put("hibernate.show_sql", true);
        props.put("hibernate.format_sql", true);

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