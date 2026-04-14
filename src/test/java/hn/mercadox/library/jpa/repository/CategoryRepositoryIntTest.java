package hn.mercadox.library.jpa.repository;


import hn.shadowcore.mercadox.library.entity.model.auth.Organization;
import hn.shadowcore.mercadox.library.entity.model.core.Category;
import hn.mercadox.library.jpa.repository.base.H2BaseJpaIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import hn.shadowcore.mercadox.library.jpa.repository.CategoryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRepositoryIntTest extends H2BaseJpaIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldRetrieveOrgCategory() {

        Organization invalidOrg = buildBaseOrganization();

        persistAll(invalidOrg);

        Category invalidCategory = Category
                .builder()
                .name("Invalid Cat.")
                .enabled(false)
                .organization(invalidOrg)
                .build();

        Category validCategory = Category.builder()
                .name("Valid Org.")
                .enabled(true)
                .organization(organization)
                .build();

        validCategory.setOrganization(organization);

        persistAll(invalidCategory, validCategory);

        List<Category> categories = categoryRepository.findAll();

        assertThat(categories).isNotEmpty();
        assertThat(categories.size()).isEqualTo(1);
        assertThat(categories.get(0)).isNotNull();
        assertThat(categories.get(0).getOrgId()).isEqualTo(organization.getId());
    }

}
