package hn.shadowcore.mercadox.library.jpa.repository;

import hn.shadowcore.mercadox.library.entity.model.ai.PlatformPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlatformPersonaRepository extends JpaRepository<PlatformPersona, UUID> {

    List<PlatformPersona> findAllByActiveTrueOrderByCreatedAtAsc();

}
