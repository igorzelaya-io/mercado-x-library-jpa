package hn.alturaforge.mercadox.library.jpa.repository.custom;

import hn.alturaforge.mercadox.library.entity.model.auth.User;

import java.util.List;

public interface CustomUserRepository {
    User findDisabledUserById(String id);

    List<User> findAllEnabledOrgAdmins();

    List<User> findAvailableDrivers();

}
