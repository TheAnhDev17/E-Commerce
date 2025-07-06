package com.example.ecommerce.exception.cart;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum CartErrorCode implements BaseErrorCode {


    CART_ITEM_NOT_FOUND(5001, "Cart item not found", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK_NOT_COMPLETE(5002,  "Insufficient stock to complete the operation.", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    CartErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
