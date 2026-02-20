package hn.shadowcore.mercadoxlibrary.jpa.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@Configuration
@EnableJpaRepositories(basePackages = "hn.shadowcore.mercadoxlibrary.jpa")
@EntityScan(basePackages = "hn.shadowcore.mercadoxlibrary.entity")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class MercadoXJpaRepositoriesAutoConfig { }