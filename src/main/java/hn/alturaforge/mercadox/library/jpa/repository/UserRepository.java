package hn.alturaforge.mercadox.library.jpa.repository;

import hn.alturaforge.mercadox.library.entity.model.auth.User;
import hn.alturaforge.mercadox.library.jpa.repository.custom.CustomUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID>, CustomUserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}