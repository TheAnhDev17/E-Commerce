package com.example.ecommerce.dto.response;

import com.example.ecommerce.entity.ProductImage;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    String name;
    String slug;
    String sku;
    BigDecimal price;
    String description;
    String shortDescription;
    BigDecimal comparePrice;

    Integer stockQuantity = 0;
    String brand;
    String model;

    Boolean isFeatured = false;
    Set<CategoryResponse> categories;
    Set<ProductImageResponse> images;
}
