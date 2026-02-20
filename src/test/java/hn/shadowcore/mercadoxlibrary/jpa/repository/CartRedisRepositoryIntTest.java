package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.response.dto.CartDto;
import hn.shadowcore.mercadoxlibrary.entity.response.dto.ItemDto;
import hn.shadowcore.mercadoxlibrary.jpa.repository.base.RedisContainerTest;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
                CartRedisRepository.class,
                RedisContainerTest.RedisContainerConfig.class
        })
class CartRedisRepositoryIntTest extends RedisContainerTest {

    @Autowired
    private CartRedisRepository redisRepository;

    private ItemDto itemDto;

    private CartDto cartDto;

    @BeforeEach
    void setEnvironment() {
        buildEnv();
    }

    @Test
    void shouldSaveAndRetrieveCart() {

        redisRepository.saveCart(cartDto);

        CartDto retrievedCart = redisRepository.getCart(user.getId().toString());

        assertThat(retrievedCart).isNotNull();
        assertThat(retrievedCart.cartItems()).isNotEmpty();
        assertThat(retrievedCart.cartItems().get(0)).isNotNull();
        assertThat(retrievedCart.cartItems().get(0).getId()).isEqualTo(itemDto.getId());

    }

    @Test
    void shouldClearCart() {

        redisRepository.saveCart(cartDto);
        redisRepository.clearCart(user.getId().toString());

        try {
           CartDto retrievedCart = redisRepository.getCart(user.getId().toString());
           fail(String
                   .format("Should've found no occurrences when calling clearCart(), found: '%s'",
                           retrievedCart.userId()));
        }
        catch(Exception e) {
            assertThat(e).isInstanceOf(ResourceNotFoundException.class);
            assertThat(e.getMessage()).isEqualTo(String
                    .format("Cart was not found for User with ID: '%s'", user.getId()));
        }
    }

    private void buildEnv() {

        user = buildBaseUser();
        user.setId(UUID.randomUUID());

        itemDto = ItemDto.builder()
                .id(UUID.randomUUID().toString())
                .name("Item")
                .description("Description")
                .categoryId("category-id")
                .unitPrice(BigDecimal.valueOf(100))
                .unitQuantity(1)
                .build();

        cartDto = new CartDto(user.getId().toString(), List.of(itemDto));
    }
}
