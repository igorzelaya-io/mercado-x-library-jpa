package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.Organization;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.User;
import hn.shadowcore.mercadoxlibrary.entity.model.auth.UserType;
import hn.shadowcore.mercadoxlibrary.entity.model.enums.UserTypeName;
import hn.shadowcore.mercadoxlibrary.jpa.repository.base.H2BaseJpaIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryIntTest extends H2BaseJpaIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnOnlyEnabledUsersForCurrentOrg() {

        User invalidUser = User.builder().username("username")
                .password("example").firstName("ex").lastName("...").email("a@b.com")
                .phoneNumber("000-000").enabled(false).isAdmin(false)
                .userType(UserType.builder()
                        .name(UserTypeName.BUYER)
                        .build())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .organization(secondaryOrg)
                .build();

        persistAll(invalidUser);
        em.flush();
        em.clear();

        List<User> results = userRepository.findAll();

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFirstName()).isEqualTo("Test");
    }

    @Test
    void findDisabledUserById() {

        User invalidUser = User.buildStaticTestUser();
        invalidUser.setEnabled(false);
        invalidUser.setOrganization(organization);

        em.persist(invalidUser);
        em.flush();
        em.clear();

        User retrievedUser = userRepository.findDisabledUserById(invalidUser.getId().toString());
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getFirstName()).isEqualTo(invalidUser.getFirstName());

    }

    @Test
    void shouldFindAllEnabledOrgAdmins() {

        Organization invalidOrg = buildBaseOrganization();

        persistAll(invalidOrg);

        User admin1 = User.buildStaticTestUser();
        admin1.setOrganization(organization);
        admin1.setIsAdmin(true);

        User admin2 = User.buildStaticTestUser();
        admin2.setUsername("normalUsername");
        admin2.setEmail("new@email.com");
        admin2.setOrganization(organization);
        admin2.setIsAdmin(true);

        User invalidAdmin = buildBaseUser();
        invalidAdmin.setOrganization(invalidOrg);
        invalidAdmin.setIsAdmin(false);

        persistAll(admin1, admin2);

        List<User> admins = userRepository.findAllEnabledOrgAdmins();

        assertThat(admins).hasSize(2);
        for(User admin : admins) {
            assertThat(admin.getOrgId()).isEqualTo(organization.getId());
        }

    }

    @Test
    void shouldFindAllAvailableDrivers() {

        UserType driver = UserType.builder().name(UserTypeName.DRIVER).build();
        persistAll(driver);

        User activatedDriver = User.buildStaticTestUser();
        activatedDriver.setUserType(driver);
        activatedDriver.setOrganization(organization);
        activatedDriver.setDriveAvailable(true);

        User inactiveDriver = buildBaseUser();
        inactiveDriver.setUsername("diffFromBase");
        inactiveDriver.setEmail("diff@example.com");
        inactiveDriver.setUserType(driver);
        inactiveDriver.setOrganization(organization);
        inactiveDriver.setDriveAvailable(false);

        persistAll(activatedDriver, inactiveDriver);

        List<User> drivers = userRepository.findAvailableDrivers();

        assertThat(drivers).hasSize(1);
        assertThat(drivers.get(0).getOrgId()).isEqualTo(organization.getId());
        assertThat(drivers.get(0).getId()).isEqualTo(activatedDriver.getId());
    }

}
