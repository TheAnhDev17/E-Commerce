package com.example.ecommerce.dto.request.order;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderItemRequest {

    @NotNull(message = "PRODUCT_ID_REQUIRED")
    Long productId;

    Long productVariant;

    @NotNull(message = "QUANTITY_REQUIRED")
    @Min(value = 1, message = "QUANTITY_MIN")
    @Max(value = 100, message = "QUANTITY_MAX")
    Integer quantity;

    BigDecimal unitPrice;

    @Size(max = 500, message = "NOTES_LENGTH_MAX")
    private String notes;
}
