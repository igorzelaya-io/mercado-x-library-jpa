package hn.shadowcore.mercadoxlibrary.jpa.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hn.shadowcore.mercadoxlibrary.entity.response.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class CartRedisRepository {

     private final RedisTemplate<String, String> redisTemplate;

     private final ObjectMapper objectMapper;

     public void saveCart(String userId, CartDto cartItems) {
         try {
             redisTemplate.opsForValue().set(getKey(userId), objectMapper
                     .writeValueAsString(cartItems), Duration.ofDays(1));
         }
         catch(JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
         }
     }

     public CartDto getCart(String userId) {
         String json = redisTemplate.opsForValue().get(getKey(userId));
         if(json == null) {
            return new CartDto(userId, new ArrayList<>());
         }
         try {
             return objectMapper.readValue(json, new TypeReference<>() {});
         }
         catch(JsonProcessingException e) {
             throw new RuntimeException(e.getMessage());
         }
     }

     public void clearCart(String userId) {
         redisTemplate.delete(getKey(userId));
     }

     private String getKey(String userId) {
         return "cart:" + userId;
     }

}
