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
    INVALID_PHONE_NUMBER_FORMAT(1019, "Invalid phone number format", HttpStatus.BAD_REQUEST),
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
