package hn.shadowcore.mercadox.library.jpa.repository.custom;

import hn.shadowcore.mercadox.library.entity.model.auth.User;

import java.util.List;

public interface CustomUserRepository {
    User findDisabledUserById(String id);

    List<User> findAllEnabledOrgAdmins();

    List<User> findAvailableDrivers();

}
