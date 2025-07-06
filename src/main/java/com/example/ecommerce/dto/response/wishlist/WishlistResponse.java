package com.example.ecommerce.dto.response.wishlist;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistResponse {
    List<WishlistItemResponse> items;
    Integer totalItems;
    LocalDateTime lastUpdated;
    Boolean isEmpty;
}
