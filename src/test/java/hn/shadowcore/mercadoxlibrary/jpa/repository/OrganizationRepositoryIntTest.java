package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.auth.Organization;
import hn.shadowcore.mercadoxlibrary.jpa.config.JpaConfig;
import hn.shadowcore.mercadoxlibrary.jpa.querydsl.OrgAwareQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = { JpaConfig.class, OrgAwareQueryFactory.class, OrganizationRepository.class })
class OrganizationRepositoryIntTest extends BaseJpaIntegrationTest {

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

        assertThat(orgs).hasSize(1);
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
