package hn.shadowcore.mercadoxlibrary.jpa.config;


import com.querydsl.jpa.impl.JPAQueryFactory;
import hn.shadowcore.mercadoxlibrary.jpa.querydsl.OrgAwareQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PredicateFactoryConfig.class)
public class QueryDSLInfrastructureConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public OrgAwareQueryFactory orgAwareQueryFactory(JPAQueryFactory deletgate) {
        return new OrgAwareQueryFactory(deletgate);
    }

}
