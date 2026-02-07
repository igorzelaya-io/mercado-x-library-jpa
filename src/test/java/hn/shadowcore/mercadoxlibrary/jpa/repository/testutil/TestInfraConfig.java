package hn.shadowcore.mercadoxlibrary.jpa.repository.testutil;


import hn.shadowcore.mercadoxcontext.config.RedisConfig;
import hn.shadowcore.mercadoxcontext.config.RedisTtlConfig;
import hn.shadowcore.mercadoxlibrary.jpa.config.JpaConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({JpaConfig.class, RedisConfig.class, RedisTtlConfig.class})
public class TestInfraConfig {
}
