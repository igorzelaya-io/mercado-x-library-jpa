package hn.shadowcore.mercadoxlibrary.jpa.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("test")
public class H2JpaTestConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:test-db;DB_CLOSE_DELAY=-1;MODE=PostgreSQL")
                .username("sa")
                .password("")
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean emf =
                new LocalContainerEntityManagerFactoryBean();

        emf.setDataSource(dataSource);
        emf.setPackagesToScan(
                "hn.shadowcore.mercadoxlibrary.entity"
        );

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        adapter.setShowSql(true);

        emf.setJpaVendorAdapter(adapter);

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "create-drop");
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

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
}
