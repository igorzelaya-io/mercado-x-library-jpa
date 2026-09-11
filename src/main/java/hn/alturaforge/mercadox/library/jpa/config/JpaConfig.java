package hn.alturaforge.mercadox.library.jpa.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("!test")
@Import(QueryDSLInfrastructureConfig.class)
@ConditionalOnProperty(
        name = "mercadox.jpa.enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class JpaConfig {

    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String user,
            @Value("${spring.datasource.password}") String password) {

        return DataSourceBuilder.create()
                .url(url)
                .username(user)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            @Value("${mercadox.jpa.entity-packages:hn.alturaforge.mercadox.library.entity}") String entityPackages,
            ConfigurableListableBeanFactory beanFactory) {
        var factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan(entityPackages.split(","));

        var vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "validate");
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.lob.non_contextual_creation", true);
        // Spring Boot's own JpaBaseConfiguration wires this automatically; this config
        // builds the EntityManagerFactory by hand and bypasses that path, so without this,
        // Hibernate can't resolve @Component-based AttributeConverters (e.g.
        // EncryptedStringConverter) that need Spring to inject their dependencies.
        props.put("hibernate.resource.beans.container", new SpringBeanContainer(beanFactory));
        factoryBean.setJpaPropertyMap(props);

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
