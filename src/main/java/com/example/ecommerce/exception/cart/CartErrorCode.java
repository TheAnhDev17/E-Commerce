package com.example.ecommerce.exception.cart;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum CartErrorCode implements BaseErrorCode {


    CART_ITEM_NOT_FOUND(5001, "Cart item not found", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK_NOT_COMPLETE(5002,  "Insufficient stock to complete the operation.", HttpStatus.BAD_REQUEST),
    CART_NOT_BELONG_TO_USER(5003, "Cart item does not belong to this user", HttpStatus.BAD_REQUEST),
    USER_NOT_LOGGED_IN_TO_USE_CART(5004, "User must be logged in to use cart", HttpStatus.UNAUTHORIZED),
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
