package com.example.ecommerce.exception.category;

import com.example.ecommerce.exception.base.BaseAppException;
import com.example.ecommerce.exception.common.CommonErrorCode;

public class CategoryException extends BaseAppException {
    public CategoryException(CategoryErrorCode errorCode){
        super(errorCode);
    }
}
