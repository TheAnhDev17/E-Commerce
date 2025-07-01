package com.example.ecommerce.exception.product;

import com.example.ecommerce.exception.base.BaseAppException;

public class ProductException extends BaseAppException {

    public ProductException(ProductErrorCode errorCode){
        super(errorCode);
    }
}

