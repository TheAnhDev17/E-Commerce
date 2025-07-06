package com.example.ecommerce.dto.response.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    List<CartItemResponse> items;
    Integer totalItems;
    Integer totalQuantity;
    BigDecimal subtotal;
    LocalDateTime lastUpdated;
    Boolean isEmpty;
}
