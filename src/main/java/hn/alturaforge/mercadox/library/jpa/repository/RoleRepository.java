package hn.alturaforge.mercadox.library.jpa.repository;

import hn.alturaforge.mercadox.library.entity.model.auth.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends BaseRepository<Role, UUID>{
    Optional<Role> findByNameIgnoreCase(String name);

}
