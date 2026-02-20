package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.model.core.Category;
import hn.shadowcore.mercadoxlibrary.entity.model.core.Inventory;
import hn.shadowcore.mercadoxlibrary.entity.model.core.Item;
import hn.shadowcore.mercadoxlibrary.jpa.repository.base.H2BaseJpaIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryIntTest extends H2BaseJpaIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    private Item item;

    private Inventory inventory;

    private Category category;

    @BeforeEach
    void buildEnv() {
        em.clear();
        em.createNativeQuery("DELETE FROM core.item").executeUpdate();
        em.createNativeQuery("DELETE FROM core.inventory").executeUpdate();
        em.createNativeQuery("DELETE FROM core.category").executeUpdate();

        buildItemEnvironment();
        persistAll(category, inventory, item);

        em.clear();
    }

    @Test
    void shouldFindItemWithInventory() {

        Item retrievedItem = itemRepository.findItemWithInventory(item.getId().toString());

        assertThat(retrievedItem).isNotNull();
        assertThat(retrievedItem.getOrgId()).isEqualTo(organization.getId());
        assertThat(retrievedItem.getInventory()).isNotNull();
        assertThat(retrievedItem.getInventory().getId()).isEqualTo(inventory.getId());
        assertThat(retrievedItem.getInventory().getOrgId()).isEqualTo(organization.getId());
    }

    @Test
    void shouldFindItemByPropagatedOrgId() {

        Inventory inv = itemRepository.findItemInventory(item.getId().toString());

        assertThat(inv).isNotNull();
        assertThat(inv.getId()).isEqualTo(inventory.getId());
        assertThat(inv.getOrgId()).isEqualTo(organization.getId());
    }

    private void buildItemEnvironment() {
        category = Category.builder()
                .name("Test Category")
                .organization(organization)
                .enabled(true)
                .build();

        inventory = Inventory.builder()
                .name("Test inventory")
                .organization(organization)
                .quantity(22)
                .category(category)
                .build();

        item = Item.builder()
                .name("Pen")
                .description("This is a very simple pen")
                .unitQuantity(12)
                .unitPrice(BigDecimal.valueOf(15))
                .inStock(true)
                .category(category)
                .inventory(inventory)
                .organization(organization)
                .build();
    }
}
