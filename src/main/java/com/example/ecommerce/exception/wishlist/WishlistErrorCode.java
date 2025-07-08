package com.example.ecommerce.exception.wishlist;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum WishlistErrorCode implements BaseErrorCode {


    WISHLIST_ITEM_NOT_FOUND(6001, "Wishlist not found", HttpStatus.BAD_REQUEST),
    WISHLIST_NOT_BELONG_TO_USER(6003, "Wishlist does not belong to this user", HttpStatus.BAD_REQUEST),
    USER_NOT_LOGGED_IN_TO_USE_WISHLIST(6004, "User must be logged in to use wishlist", HttpStatus.UNAUTHORIZED),
    WISHLIST_ALREADY_HAS_THIS_PRODUCT(6004, "Product is already in wishlist", HttpStatus.BAD_REQUEST)
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    WishlistErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
