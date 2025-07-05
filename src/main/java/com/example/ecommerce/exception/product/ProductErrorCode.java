package com.example.ecommerce.exception.product;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ProductErrorCode implements BaseErrorCode {


    PRODUCT_NOT_FOUND(3001, "User not existed", HttpStatus.NOT_FOUND),
    SKU_EXISTED(3002, "SKU already existed", HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID_CATEGORIES(3003, "Some categories were not found or inactive", HttpStatus.BAD_REQUEST),
    PRODUCT_CATEGORY_INACTIVE(3004, "Category is inactive", HttpStatus.BAD_REQUEST),
    VARIANT_SKU_REQUIRED(3005, "Product variant sku is required", HttpStatus.BAD_REQUEST),
    VARIANT_PRICE(3006, "Product variant price must be non-negative", HttpStatus.BAD_REQUEST),
    VARIANT_COMPARE_PRICE(3007, "Product variant compare price must be non-negative", HttpStatus.BAD_REQUEST),
    VARIANT_STOCK_QUANTITY(3008, "Stock quantity must be non-negative", HttpStatus.BAD_REQUEST),
    VARIANT_SKU_EXISTED(3005, "Product variant sku existed", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ProductErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
