package hn.shadowcore.mercadox.library.jpa.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("hn.shadowcore.mercadox.library.jpa")
@EnableJpaRepositories(basePackages = "hn.shadowcore.mercadox.library.jpa.repository")
@EntityScan(basePackages = "hn.shadowcore.mercadox.library.entity")
@EnableTransactionManagement
public class MercadoXJpaScanningConfig { }
