package hn.shadowcore.mercadoxlibrary.jpa.config;

import hn.shadowcore.mercadoxlibrary.jpa.filter.HibernateFilterIntegrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HibernateFilterCustomizer {

    @Bean
    public IntegratorProvider integratorProvider() {
        return () -> List.of(new HibernateFilterIntegrator());
    }

}
