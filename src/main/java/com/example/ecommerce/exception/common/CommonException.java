package com.example.ecommerce.exception.common;

import com.example.ecommerce.exception.base.BaseAppException;
import com.example.ecommerce.exception.base.BaseErrorCode;

public class CommonException extends BaseAppException {
    public CommonException(CommonErrorCode errorCode){
        super(errorCode);
    }
}
