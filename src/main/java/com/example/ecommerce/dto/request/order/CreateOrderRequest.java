package com.example.ecommerce.dto.request.order;

import com.example.ecommerce.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

    String userId;


    List<CreateOrderItemRequest> orderItems;

    // Shipping info
    @NotBlank
    @Size(max = 500)
    String shippingAddress;

    @NotBlank
    @Size(max = 100)
    String shippingCity;

    @Size(max = 100)
    String shippingDistrict;

    @Size(max = 100)
    String shippingWard;

    @Size(max = 20)
    String shippingPostalCode;

    // Recipient Information
    @NotBlank
    @Size(max = 100)
    String recipientName;

    @NotBlank
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "INVALID_PHONE_NUMBER_FORMAT")
    @Size(max = 20)
    String recipientPhone;

    @Email
    @Size(max = 100)
    String recipientEmail;

    // Payment information
    @NotNull
    PaymentMethod paymentMethod;

    @Size(max = 1000)
    String notes;

    @Size(max = 50)
    String couponCode;

    @Size(max = 500)
    String cancellationReason;
}
