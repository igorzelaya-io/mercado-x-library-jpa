package hn.mercadox.library.jpa.repository;

import hn.shadowcore.mercadox.library.entity.model.auth.Organization;
import hn.mercadox.library.jpa.repository.base.H2BaseJpaIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import hn.shadowcore.mercadox.library.jpa.repository.OrganizationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationRepositoryIntTest extends H2BaseJpaIntegrationTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    void shouldReturnEnabledOrgFilter() {

        Organization invalidOrg = buildBaseOrganization();
        invalidOrg.setEnabled(false);
        invalidOrg.setName("Invalid Org");

        em.persist(invalidOrg);
        em.flush();
        em.clear();

        List<Organization> orgs = organizationRepository.findAll();

        assertThat(orgs).hasSize(2);
        assertThat(orgs.get(0).getName()).isEqualTo(organization.getName());

    }

    @Test
    void shouldReturnInactiveOrgByID() {

        Organization org = buildBaseOrganization();
        org.setEnabled(false);

        em.persist(org);
        em.flush();
        em.clear();

        Organization invalidOrg = organizationRepository.findInactiveOrgById(org.getId().toString());

        assertThat(invalidOrg).isNotNull();
        assertThat(invalidOrg.getEnabled()).isFalse();
        assertThat(invalidOrg.getId()).isEqualTo(org.getId());

    }

}
