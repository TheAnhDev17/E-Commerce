package com.example.ecommerce.exception.cart;

import com.example.ecommerce.exception.base.BaseAppException;
import com.example.ecommerce.exception.product.ProductErrorCode;

public class CartException extends BaseAppException {

    public CartException(CartErrorCode errorCode){
        super(errorCode);
    }
}

