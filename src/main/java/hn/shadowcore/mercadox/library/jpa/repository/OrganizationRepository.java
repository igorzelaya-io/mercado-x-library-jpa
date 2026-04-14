package hn.shadowcore.mercadox.library.jpa.repository;

import hn.shadowcore.mercadox.library.jpa.repository.custom.CustomOrgRepository;
import hn.shadowcore.mercadox.library.entity.model.auth.Organization;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends BaseRepository<Organization, UUID>, CustomOrgRepository {
    Optional<Organization> findByNameContainingIgnoreCase(String name);

}
