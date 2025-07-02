package com.example.ecommerce.exception.file;

import com.example.ecommerce.exception.base.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum FileErrorCode implements BaseErrorCode {

    FILE_UPLOAD_FAILED(4001, "Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND(4002, "File not found", HttpStatus.NOT_FOUND),
    FILE_SIZE_EXCEEDED(4003, "File size exceeded maximum limit", HttpStatus.BAD_REQUEST),
    INVALID_FILE_TYPE(4004, "Invalid file type", HttpStatus.BAD_REQUEST),
    FILE_STORAGE_ERROR(4005, "File storage error", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_EMPTY(4006, "File not found", HttpStatus.NOT_FOUND),
    INVALID_FILE_EXTENSION(4007, "Invalid file extension", HttpStatus.BAD_REQUEST)
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    FileErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
