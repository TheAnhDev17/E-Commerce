package com.example.ecommerce.exception.base;

import org.springframework.http.HttpStatusCode;

public interface BaseErrorCode {
    int getCode();
    String getMessage();
    HttpStatusCode getStatusCode();
}
