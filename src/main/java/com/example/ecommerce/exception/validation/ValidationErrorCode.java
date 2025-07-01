package com.example.ecommerce.exception.validation;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ValidationErrorCode implements BaseErrorCode {

    // User validation
    USERNAME_INVALID(1003, "Username must be at least {min} character", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} character", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1005, "Email format is invalid", HttpStatus.BAD_REQUEST),
    DOB_INVALID(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),

    // Product validation
    PRODUCT_NAME_REQUIRED(1012, "Product name is required", HttpStatus.BAD_REQUEST),
    PRODUCT_SKU_REQUIRED(1013, "Product sku is required", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_REQUIRED(1014, "Product price is required", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_MIN(1015, "Price must be greater than 0", HttpStatus.BAD_REQUEST),

    // Category validation
    CATEGORY_NAME_REQUIRED(1011, "Category name is required", HttpStatus.BAD_REQUEST),

    // Common validation
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ValidationErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
