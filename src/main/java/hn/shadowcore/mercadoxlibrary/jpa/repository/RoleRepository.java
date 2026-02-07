package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends BaseRepository<Role, UUID>{
    Optional<Role> findByNameIgnoreCase(String name);

}
