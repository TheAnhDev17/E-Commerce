package com.example.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageResponse {
    Long id;
    String imageUrl;
    String altText;
    Integer sortOrder = 0;
    Boolean isPrimary = false;
}
