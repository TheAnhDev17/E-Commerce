package com.example.ecommerce.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = Exception.class)
    ResponseEntity<String> handlingException(Exception exception){

        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        return ResponseEntity.status(errorCode.getStatusCode()).body(errorCode.getMessage());
    }

    @ExceptionHandler(exception = AppException.class)
    ResponseEntity<String> handlingAppException(AppException exception){

        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity.status(errorCode.getStatusCode()).body(errorCode.getMessage());
    }
}
