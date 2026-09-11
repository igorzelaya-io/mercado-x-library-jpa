package hn.alturaforge.mercadox.library.jpa.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("hn.alturaforge.mercadox.library.jpa")
@EnableJpaRepositories(basePackages = "hn.alturaforge.mercadox.library.jpa.repository")
@EntityScan(basePackages = "hn.alturaforge.mercadox.library.entity")
@EnableTransactionManagement
public class MercadoXJpaScanningConfig { }
