package com.example.ecommerce.exception.file;

import com.example.ecommerce.exception.base.BaseAppException;

public class FileException extends BaseAppException {

    public FileException(FileErrorCode errorCode){
        super(errorCode);
    }

    // Constructor with cause để preserve original exception
    public FileException(FileErrorCode errorCode, Throwable cause) {
        super(errorCode);
        initCause(cause);
    }
}
