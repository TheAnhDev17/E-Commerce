package com.example.ecommerce.exception.validation;

import com.example.ecommerce.exception.base.BaseAppException;

public class ValidationException extends BaseAppException {
    public ValidationException(ValidationErrorCode errorCode){
        super(errorCode);
    }
}
