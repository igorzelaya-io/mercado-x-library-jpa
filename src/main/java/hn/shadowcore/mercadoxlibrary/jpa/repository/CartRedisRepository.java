package hn.shadowcore.mercadoxlibrary.jpa.repository;

import hn.shadowcore.mercadoxlibrary.entity.response.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRedisRepository {

     private final RedisTemplate<String, Object> redisTemplate;

     public void saveCart(CartDto cartItems) {
         redisTemplate.opsForValue().set(getKey(cartItems.userId()), cartItems, Duration.ofDays(1));
     }

     public CartDto getCart(String userId) {
         return Optional.ofNullable(redisTemplate.opsForValue().get(getKey(userId)))
                 .filter(CartDto.class::isInstance)
                 .map(CartDto.class::cast)
                 .orElseThrow(() -> new ResourceNotFoundException(String
                         .format("Cart was not found for User with ID: '%s'", userId)));
     }

     public void clearCart(String userId) {
         redisTemplate.delete(getKey(userId));
     }

     private String getKey(String userId) {
         return "cart:" + userId;
     }

}
