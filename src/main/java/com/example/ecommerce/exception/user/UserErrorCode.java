package com.example.ecommerce.exception.user;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum UserErrorCode implements BaseErrorCode {

    USER_EXISTED(2002, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(2005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(2006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2007, "You do not have permission", HttpStatus.FORBIDDEN),
    TOKEN_CREATION_FAILED(2010, "Token creation failed", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    UserErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
