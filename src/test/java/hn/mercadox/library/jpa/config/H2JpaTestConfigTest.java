package hn.mercadox.library.jpa.config;

import hn.alturaforge.mercadox.library.entity.crypto.MasterKeyService;
import hn.alturaforge.mercadox.library.jpa.config.H2JpaTestConfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class H2JpaTestConfigTest {

    @Test
    void masterKeyService_wrapsAndUnwrapsAnAes256DataKey() {
        MasterKeyService masterKeyService = new H2JpaTestConfig().masterKeyService();
        byte[] dataKey = new byte[32];

        byte[] wrappedDataKey = masterKeyService.wrap(dataKey);

        assertThat(masterKeyService.unwrap(wrappedDataKey)).isEqualTo(dataKey);
    }
}
