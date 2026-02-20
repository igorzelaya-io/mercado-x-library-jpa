package hn.shadowcore.mercadoxlibrary.jpa.config;


import hn.shadowcore.mercadoxlibrary.jpa.predicate.ItemPredicateFactory;
import hn.shadowcore.mercadoxlibrary.jpa.predicate.OrgPredicateFactory;
import hn.shadowcore.mercadoxlibrary.jpa.predicate.UserPredicateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PredicateFactoryConfig {

    @Bean
    public ItemPredicateFactory itemPredicateFactory() {
        return new ItemPredicateFactory();
    }

    @Bean
    public OrgPredicateFactory orgPredicateFactory() {
        return new OrgPredicateFactory();
    }

    @Bean
    public UserPredicateFactory userPredicateFactory() {
        return new UserPredicateFactory();
    }

}
