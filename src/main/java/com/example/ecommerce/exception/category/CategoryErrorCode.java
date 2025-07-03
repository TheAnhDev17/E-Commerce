package com.example.ecommerce.exception.category;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum CategoryErrorCode implements BaseErrorCode {

    CATEGORY_NAME_REQUIRED(4001, "Category name is required", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(4002, "Category not existed", HttpStatus.NOT_FOUND),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    CategoryErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
