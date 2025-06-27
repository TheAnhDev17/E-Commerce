package com.example.ecommerce.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} character", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} character", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    DOB_INVALID(1008, "Your age must be at least {min}", HttpStatus.FORBIDDEN),
    TOKEN_CREATION_FAILED(1010, "Token creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_NAME_REQUIRED(1011, "Category name is required", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_REQUIRED(1012, "Product name is required", HttpStatus.BAD_REQUEST),
    PRODUCT_SKU_REQUIRED(1013, "Product sku is required", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_REQUIRED(1014, "Product price is required", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_MIN(1015, "Price must be greater than 0", HttpStatus.BAD_REQUEST),
    ;



    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
