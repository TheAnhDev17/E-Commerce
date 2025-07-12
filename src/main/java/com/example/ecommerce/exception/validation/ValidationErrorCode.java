package com.example.ecommerce.exception.validation;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ValidationErrorCode implements BaseErrorCode {

    // User validation
    USERNAME_INVALID(1002, "Username must be at least {min} character", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must be at least {min} character", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1004, "Email format is invalid", HttpStatus.BAD_REQUEST),
    DOB_INVALID(1005, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),

    // Product validation
    PRODUCT_NAME_REQUIRED(1006, "Product name is required", HttpStatus.BAD_REQUEST),
    PRODUCT_SKU_REQUIRED(1007, "Product sku is required", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_REQUIRED(1008, "Product price is required", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_MIN(1009, "Price must be greater than 0", HttpStatus.BAD_REQUEST),

    // Category validation
    CATEGORY_NAME_REQUIRED(1010, "Category name is required", HttpStatus.BAD_REQUEST),

    // Common validation
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),

    // Cart validation
    QUANTITY_MIN_INVALID(1011, "Quantity must be at least {min}", HttpStatus.BAD_REQUEST),
    QUANTITY_MAX_INVALID(1012, "Quantity cannot exceed {max}", HttpStatus.BAD_REQUEST),
    PRODUCT_ID_REQUIRED(1013, "Product ID is required", HttpStatus.BAD_REQUEST),
    CART_ITEM_ID_REQUIRED(1014, "Cart item ID is required", HttpStatus.BAD_REQUEST),

    // Product variant validation
    VARIANT_SKU_REQUIRED(1015, "Product variant sku is required", HttpStatus.BAD_REQUEST),
    VARIANT_PRICE_NON_NEGATIVE(1016, "Product variant price must be non-negative", HttpStatus.BAD_REQUEST),
    VARIANT_COMPARE_PRICE_NON_NEGATIVE(1017, "Product variant compare price must be non-negative", HttpStatus.BAD_REQUEST),
    VARIANT_STOCK_QUANTITY_NON_NEGATIVE(1018, "Stock quantity must be non-negative", HttpStatus.BAD_REQUEST),


    // Order
    QUANTITY_REQUIRED(1020, "Quantity is required", HttpStatus.BAD_REQUEST),
    QUANTITY_MIN(1021, "Quantity must be at least {min}", HttpStatus.BAD_REQUEST),
    QUANTITY_MAX(1022, "Quantity cannot exceed {max}", HttpStatus.BAD_REQUEST),
    NOTES_LENGTH_MAX(1023, "Item notes must not exceed {max} characters", HttpStatus.BAD_REQUEST),

    ORDER_ITEMS_REQUIRED(1024, "Order items cannot be null", HttpStatus.BAD_REQUEST),
    ORDER_ITEMS_NOT_EMPTY(1025, "Order items cannot be empty", HttpStatus.BAD_REQUEST),
    ORDER_ITEMS_VALIDATE_SIZE(1026, "Order must contain between {min} and {max} items", HttpStatus.BAD_REQUEST),

    SHIPPING_ADDRESS_REQUIRED(1027, "Shipping address is required", HttpStatus.BAD_REQUEST),
    SHIPPING_ADDRESS_LENGTH_MAX(1028, "Shipping address must not exceed {max} characters", HttpStatus.BAD_REQUEST),

    SHIPPING_CITY_REQUIRED(1029, "City is required", HttpStatus.BAD_REQUEST),
    SHIPPING_CITY_LENGTH_MAX(1030, "City must not exceed {max} characters", HttpStatus.BAD_REQUEST),

    SHIPPING_DISTRICT_LENGTH_MAX(1031, "District must not exceed {max} characters", HttpStatus.BAD_REQUEST),

    SHIPPING_WARD_LENGTH_MAX(1032, "Ward must not exceed {max} characters", HttpStatus.BAD_REQUEST),

    POSTAL_CODE_LENGTH_MAX(1033, "Postal code must not exceed {max} characters", HttpStatus.BAD_REQUEST),

    RECIPIENT_NAME_REQUIRED(1034, "Recipient name is required", HttpStatus.BAD_REQUEST),
    RECIPIENT_NAME_LENGTH_MAX(1035, "Recipient name must not exceed {max} characters", HttpStatus.BAD_REQUEST),


    RECIPIENT_PHONE_REQUIRED(1036, "Recipient phone is required", HttpStatus.BAD_REQUEST),
    RECIPIENT_PHONE_LENGTH_MAX(1037, "Recipient phone must not exceed {max} characters", HttpStatus.BAD_REQUEST),
    RECIPIENT_PHONE_INVALID_FORMAT(1019, "Invalid phone number format", HttpStatus.BAD_REQUEST),

    RECIPIENT_EMAIL_INVALID_FORMAT(1038, "Invalid email format", HttpStatus.BAD_REQUEST),
    RECIPIENT_EMAIL_LENGTH_MAX(1039, "Recipient email must not exceed {max} characters", HttpStatus.BAD_REQUEST),

    PAYMENT_METHOD_REQUIRED(1040, "Payment method is required", HttpStatus.BAD_REQUEST),

    COUPON_CODE_LENGTH_MAX(1041, "Coupon code must not exceed {max} characters", HttpStatus.BAD_REQUEST),

    CANCELLATION_REASON_LENGTH_MAX(1042, "Cancellation reason must not exceed {max} characters", HttpStatus.BAD_REQUEST),

    ;


    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ValidationErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
