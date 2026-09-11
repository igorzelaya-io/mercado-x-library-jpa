package hn.alturaforge.mercadox.library.jpa.repository;

import hn.alturaforge.mercadox.library.jpa.repository.custom.CustomOrgRepository;
import hn.alturaforge.mercadox.library.entity.model.auth.Organization;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends BaseRepository<Organization, UUID>, CustomOrgRepository {
    Optional<Organization> findByNameContainingIgnoreCase(String name);

}
