package hn.mercadox.library.jpa.repository.base;


import hn.alturaforge.mercadox.context.utils.OrgIdContextHolder;
import hn.alturaforge.mercadox.library.jpa.aspect.HibernateFilterAspect;
import hn.alturaforge.mercadox.library.jpa.aspect.HibernateFilterDisablingAspect;
import hn.alturaforge.mercadox.library.jpa.config.H2JpaTestConfig;
import hn.alturaforge.mercadox.library.jpa.config.MercadoXJpaScanningConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
@ContextConfiguration( classes = {
        H2JpaTestConfig.class,
        MercadoXJpaScanningConfig.class,
        HibernateFilterAspect.class,
        HibernateFilterDisablingAspect.class
})
// Not a @SpringBootTest, so no application.yml is loaded automatically — this narrow
// context config needs its own explicit property source for the required master key.
@TestPropertySource(properties = "encryption.master-key.value=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class H2BaseJpaIntegrationTest extends AbstractIntegrationTest {

    @BeforeEach
    void cleanDatabase() {
        em.createNativeQuery("DELETE FROM core.orders").executeUpdate();
        em.createNativeQuery("DELETE FROM auth.users").executeUpdate();
        em.createNativeQuery("DELETE FROM auth.organization").executeUpdate();

        em.flush();
        em.clear();

        organization = buildBaseOrganization();
        secondaryOrg = buildSecondaryOrganization();

        persistAll(organization, secondaryOrg);

        OrgIdContextHolder.setTenantId(organization.getId().toString());

        user = buildBaseUser();
        persistAll(user);

        order = buildBaseOrder();
        persistAll(order);

        em.clear();
    }
}
