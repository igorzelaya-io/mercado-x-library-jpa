package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.Organization;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends BaseRepository<Organization, UUID>, CustomOrgRepository {
    Optional<Organization> findByNameContainingIgnoreCase(String name);

}
