package com.example.ecommerce.dto.request.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCartItemRequest {

    @NotNull(message = "CART_ITEM_ID_REQUIRED")
    Long cartItemId;

    @Min(value = 1, message = "QUANTITY_MIN_INVALID")
    @Max(value = 100, message = "QUANTITY_MAX_INVALID")
    Integer quantity;
}
