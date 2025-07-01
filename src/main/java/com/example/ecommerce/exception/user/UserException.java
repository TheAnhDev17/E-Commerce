package com.example.ecommerce.exception.user;

import com.example.ecommerce.exception.base.BaseAppException;

public class UserException extends BaseAppException {
    public UserException(UserErrorCode errorCode){
        super(errorCode);
    }
}
