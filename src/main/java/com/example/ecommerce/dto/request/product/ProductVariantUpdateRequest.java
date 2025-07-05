package com.example.ecommerce.dto.request.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantUpdateRequest {
    @NotBlank(message = "VARIANT_SKU_REQUIRED")
    String sku;

    String nameSuffix;

    @DecimalMin(value = "0.0", message = "VARIANT_PRICE")
    BigDecimal price;

    @DecimalMin(value = "0.0", message = "VARIANT_COMPARE_PRICE")
    BigDecimal comparePrice;

    @Builder.Default
    @Min(value = 0, message = "VARIANT_STOCK_QUANTITY")
    Integer stockQuantity = 0;

    Map<String, Object> attributes;

    @Builder.Default
    Boolean isActive = true;
}
