package com.example.ecommerce.exception;

import com.example.ecommerce.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(exception = Exception.class)
    ResponseEntity<ApiResponse<Void>> handlingException(Exception exception){
        log.error("Unhandled exception: ", exception);
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(exception = AppException.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(AppException exception){

        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler({AuthorizationDeniedException.class, AccessDeniedException.class})
    ResponseEntity<ApiResponse<Void>> handlingAuthorizationDenied(Exception exception){

        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handlingMethodArgumentNotValidException (MethodArgumentNotValidException exception){

        String enumKey = Optional.ofNullable(exception.getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElse("INVALID_KEY");

        ErrorCode errorCode = parseErrorCode(enumKey);

        Map<String, Object> attributes = extractConstraintAttributes(exception);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(buildErrorMessage(errorCode, attributes))
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private Map<String, Object> extractConstraintAttributes(MethodArgumentNotValidException exception){
        try {
            return exception.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .findFirst()
                    .map(error -> {
                        ConstraintViolation<?> violation = error.unwrap(ConstraintViolation.class);
                        return violation.getConstraintDescriptor().getAttributes();
                    })
                    .orElse(Collections.emptyMap());

        } catch (Exception e){
            log.debug("Failed to extract constraint attributes", e);
            return Collections.emptyMap();
        }
    }

    private ErrorCode parseErrorCode(String enumKey){
        try {
            return ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid error code: {}, using default", enumKey);
            return ErrorCode.INVALID_KEY;
        }
    }

    private String buildErrorMessage(ErrorCode errorCode, Map<String, Object> attributes){
        return attributes.isEmpty()
                ? errorCode.getMessage()
                : mapAttribute(errorCode.getMessage(), attributes);
    }

    private String mapAttribute(String message, Map<String, Object> attributes){
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
