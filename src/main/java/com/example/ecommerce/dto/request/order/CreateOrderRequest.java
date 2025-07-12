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

    String userId; // Optional - có thể lấy từ Authentication

    // Order Items
    @NotNull(message = "ORDER_ITEMS_REQUIRED")
    @NotEmpty(message = "ORDER_ITEMS_NOT_EMPTY")
    @Size(min = 1, max = 50, message = "ORDER_ITEMS_VALIDATE_SIZE")
    List<CreateOrderItemRequest> orderItems;

    // Shipping info
    @NotBlank(message = "SHIPPING_ADDRESS_REQUIRED")
    @Size(max = 500, message = "SHIPPING_ADDRESS_LENGTH_MAX")
    String shippingAddress;

    @NotBlank(message = "SHIPPING_CITY_REQUIRED")
    @Size(max = 100, message = "SHIPPING_CITY_LENGTH_MAX")
    String shippingCity;

    @Size(max = 100, message = "SHIPPING_DISTRICT_LENGTH_MAX")
    String shippingDistrict;

    @Size(max = 100, message = "SHIPPING_WARD_LENGTH_MAX")
    String shippingWard;

    @Size(max = 20, message = "POSTAL_CODE_LENGTH_MAX")
    String shippingPostalCode;

    // Recipient Information
    @NotBlank(message = "RECIPIENT_NAME_REQUIRED")
    @Size(max = 100, message = "RECIPIENT_NAME_LENGTH_MAX")
    String recipientName;

    @NotBlank(message = "RECIPIENT_PHONE_REQUIRED")
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "RECIPIENT_PHONE_INVALID_FORMAT")
    @Size(max = 20, message = "RECIPIENT_PHONE_LENGTH_MAX")
    String recipientPhone;

    @Email(message = "RECIPIENT_EMAIL_INVALID_FORMAT")
    @Size(max = 100, message = "RECIPIENT_EMAIL_LENGTH_MAX")
    String recipientEmail;

    // Payment information
    @NotNull(message = "PAYMENT_METHOD_REQUIRED")
    PaymentMethod paymentMethod;

    @Size(max = 1000, message = "NOTES_LENGTH_MAX")
    String notes;

    @Size(max = 50, message = "COUPON_CODE_LENGTH_MAX")
    String couponCode;

    @Size(max = 500, message = "CANCELLATION_REASON_LENGTH_MAX")
    String cancellationReason;
}
