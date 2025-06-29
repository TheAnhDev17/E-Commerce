package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    @NotBlank(message = "PRODUCT_NAME_REQUIRED")
    String name;

    @NotBlank(message = "PRODUCT_SKU_REQUIRED")
    String sku;

    @NotNull(message = "PRODUCT_PRICE_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = false, message = "PRODUCT_PRICE_MIN")
    BigDecimal price;

    String description;
    String shortDescription;
    BigDecimal comparePrice;
    Integer stockQuantity = 0;
    String brand;
    String model;
    Boolean isFeatured = false;

    List<Long> categoryIds;
}
