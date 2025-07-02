package com.example.ecommerce.dto.request.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageCreateRequest {
    String imageUrl;

    String altText;

    @Builder.Default
    Integer sortOrder = 0;

    @Builder.Default
    Boolean isPrimary = false;
}
