package hn.alturaforge.mercadox.library.jpa.repository;

import hn.alturaforge.mercadox.library.entity.model.ai.OrganizationPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizationPersonaRepository extends JpaRepository<OrganizationPersona, UUID> {

    List<OrganizationPersona> findAllByOrganizationIdAndActiveTrueOrderByCreatedAtAsc(UUID organizationId);

}
