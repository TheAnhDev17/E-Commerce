package com.example.ecommerce.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponse {
    private Long id;
    private String sku;
    private String nameSuffix;
    private String displayName;
    private BigDecimal price;
    private BigDecimal comparePrice;
    private BigDecimal effectivePrice;
    private BigDecimal effectiveComparePrice;
    private Integer stockQuantity;
    private Map<String, Object> attributes;
    private Boolean isActive;
    private Boolean isInStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}