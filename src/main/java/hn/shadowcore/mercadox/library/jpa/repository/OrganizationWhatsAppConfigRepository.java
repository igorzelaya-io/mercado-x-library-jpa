package hn.shadowcore.mercadox.library.jpa.repository;

import hn.shadowcore.mercadox.library.entity.model.ai.OrganizationWhatsAppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationWhatsAppConfigRepository extends JpaRepository<OrganizationWhatsAppConfig, UUID> {

    Optional<OrganizationWhatsAppConfig> findByPhoneNumberId(String phoneNumberId);

    Optional<OrganizationWhatsAppConfig> findByOrganizationId(UUID organizationId);

}
