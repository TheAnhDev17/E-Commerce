package com.example.ecommerce.exception.wishlist;

import com.example.ecommerce.exception.base.BaseAppException;
import com.example.ecommerce.exception.cart.CartErrorCode;

public class WishlistException extends BaseAppException {

    public WishlistException(WishlistErrorCode errorCode){
        super(errorCode);
    }
}

