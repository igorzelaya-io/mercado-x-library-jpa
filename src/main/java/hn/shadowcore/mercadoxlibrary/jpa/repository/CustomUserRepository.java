package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;

import java.util.List;

public interface CustomUserRepository {
    User findDisabledUserById(String id);

    List<User> findAllEnabledOrgAdmins();

    List<User> findAvailableDrivers();

}
