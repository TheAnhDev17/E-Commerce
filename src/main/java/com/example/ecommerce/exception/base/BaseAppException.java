package com.example.ecommerce.exception.base;

import lombok.Getter;

@Getter
public abstract class BaseAppException extends RuntimeException {

    private final BaseErrorCode errorCode;

    protected BaseAppException(BaseErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
