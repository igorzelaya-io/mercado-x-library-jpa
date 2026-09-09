package hn.mercadox.library.jpa.config;

import hn.shadowcore.mercadox.library.jpa.config.JpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class JpaConfigTest {

    @Test
    void entityManagerFactory_registersSpringBeanContainerForAttributeConverters() {
        DataSource dataSource = mock(DataSource.class);
        ConfigurableListableBeanFactory beanFactory = mock(ConfigurableListableBeanFactory.class);

        LocalContainerEntityManagerFactoryBean factoryBean = new JpaConfig()
                .entityManagerFactory(dataSource, "hn.shadowcore.mercadox.library.entity", beanFactory);

        assertThat(factoryBean.getDataSource()).isSameAs(dataSource);
        assertThat(factoryBean.getJpaPropertyMap())
                .containsEntry("hibernate.hbm2ddl.auto", "validate")
                .containsEntry("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .containsEntry("hibernate.lob.non_contextual_creation", true);
        assertThat(factoryBean.getJpaPropertyMap().get("hibernate.resource.beans.container"))
                .isInstanceOf(SpringBeanContainer.class);
    }
}
