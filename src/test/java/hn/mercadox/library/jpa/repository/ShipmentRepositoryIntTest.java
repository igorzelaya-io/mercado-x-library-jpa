package hn.mercadox.library.jpa.repository;

import hn.mercadox.library.jpa.repository.base.H2BaseJpaIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import hn.alturaforge.mercadox.library.jpa.repository.ShipmentRepository;

class ShipmentRepositoryIntTest extends H2BaseJpaIntegrationTest {

    @Autowired
    private ShipmentRepository shipmentRepository;


}
