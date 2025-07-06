package com.example.ecommerce.exception.product;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ProductErrorCode implements BaseErrorCode {


    PRODUCT_NOT_FOUND(3001, "Product not found", HttpStatus.NOT_FOUND),
    SKU_EXISTED(3002, "SKU already existed", HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID_CATEGORIES(3003, "Some categories were not found or inactive", HttpStatus.BAD_REQUEST),
    PRODUCT_CATEGORY_INACTIVE(3004, "Category is inactive", HttpStatus.BAD_REQUEST),
    VARIANT_SKU_EXISTED(3005, "Product variant sku existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_AVAILABLE(3006, "Product is not available for purchase", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_NOT_FOUND(3007, "Product variant not found", HttpStatus.NOT_FOUND),
    PRODUCT_VARIANT_NOT_BELONG_TO_PRODUCT(3008, "Product variant does not belong to this product", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_NOT_AVAILABLE(3009, "Product variant is not available", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_INSUFFICIENT_STOCK(3010, "Insufficient stock.", HttpStatus.BAD_REQUEST),
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
