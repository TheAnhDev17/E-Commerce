package com.example.ecommerce.exception.order;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum OrderErrorCode implements BaseErrorCode {


    PRODUCT_NOT_FOUND(7001, "Product not found", HttpStatus.NOT_FOUND),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    OrderErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
