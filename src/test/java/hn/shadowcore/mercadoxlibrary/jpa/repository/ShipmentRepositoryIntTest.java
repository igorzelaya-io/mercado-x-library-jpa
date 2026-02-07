package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.jpa.config.JpaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = { JpaConfig.class, ShipmentRepository.class })
class ShipmentRepositoryIntTest extends BaseJpaIntegrationTest {

    @Autowired
    private ShipmentRepository shipmentRepository;


    


}
