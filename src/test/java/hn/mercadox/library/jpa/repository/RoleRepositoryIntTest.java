package hn.mercadox.library.jpa.repository;

import hn.shadowcore.mercadox.library.entity.model.auth.Organization;
import hn.shadowcore.mercadox.library.entity.model.auth.Role;
import hn.mercadox.library.jpa.repository.base.H2BaseJpaIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import hn.shadowcore.mercadox.library.jpa.repository.RoleRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleRepositoryIntTest extends H2BaseJpaIntegrationTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldReturnRoleForCurrentOrg() {

        Organization invalidOrg = buildBaseOrganization();

        em.persist(invalidOrg);
        em.flush();

        Role role = Role.builder().name("ROLE_USER").organization(organization).build();
        Role invalidRole = Role.builder().name("ROLE_ADMIN").organization(invalidOrg).build();

        invalidRole.setOrganization(invalidOrg);

        em.persist(role);
        em.persist(invalidRole);
        em.flush();
        em.clear();

        List<Role> roles = roleRepository.findAll();

        assertThat(roles).hasSize(1);
        assertThat(roles.get(0).getName()).isEqualTo("ROLE_USER");

    }

}
