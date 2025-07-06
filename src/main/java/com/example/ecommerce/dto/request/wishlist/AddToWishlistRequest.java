package com.example.ecommerce.dto.request.wishlist;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddToWishlistRequest {

    @NotNull(message = "PRODUCT_ID_REQUIRED")
    Long productId;
}
