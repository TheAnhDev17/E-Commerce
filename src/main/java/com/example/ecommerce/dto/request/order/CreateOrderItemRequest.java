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

    @NotNull
    Long productId;

    Long productVariant;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    Integer quantity;

    BigDecimal unitPrice;

    @Size(max = 500)
    private String notes;
}
