package hn.shadowcore.mercadox.library.jpa.repository;

import hn.shadowcore.mercadox.library.entity.model.core.Shipment;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShipmentRepository extends BaseRepository<Shipment, UUID>{ }
