package com.example.ecommerce.exception.order;

import com.example.ecommerce.exception.base.BaseAppException;
import com.example.ecommerce.exception.product.ProductErrorCode;

public class OrderException extends BaseAppException {

    public OrderException(OrderErrorCode errorCode){
        super(errorCode);
    }
}

