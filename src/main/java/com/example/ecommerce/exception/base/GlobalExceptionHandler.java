package com.example.ecommerce.exception.base;

import com.example.ecommerce.dto.response.common.ApiResponse;
import com.example.ecommerce.exception.common.CommonErrorCode;
import com.example.ecommerce.exception.user.UserErrorCode;
import com.example.ecommerce.exception.validation.ValidationErrorCode;
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
    private static final String MAX_ATTRIBUTE = "max";

    @ExceptionHandler(exception = Exception.class)
    ResponseEntity<ApiResponse<Void>> handlingException(Exception exception){
        log.error("Unhandled exception: ", exception);
        BaseErrorCode errorCode = CommonErrorCode.UNCATEGORIZED_EXCEPTION;
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(exception = BaseAppException.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(BaseAppException exception){

        BaseErrorCode errorCode = exception.getErrorCode();
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler({AuthorizationDeniedException.class, AccessDeniedException.class})
    ResponseEntity<ApiResponse<Void>> handlingAuthorizationDenied(Exception exception){

        BaseErrorCode errorCode = UserErrorCode.UNAUTHORIZED;
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

        BaseErrorCode errorCode = parseErrorCode(enumKey);

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

    private BaseErrorCode parseErrorCode(String enumKey){
        try {
            return ValidationErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid error code: {}, using default", enumKey);
            return ValidationErrorCode.INVALID_KEY;
        }
    }

    private String buildErrorMessage(BaseErrorCode errorCode, Map<String, Object> attributes){
        return attributes.isEmpty()
                ? errorCode.getMessage()
                : mapAttribute(errorCode.getMessage(), attributes);
    }

    private String mapAttribute(String message, Map<String, Object> attributes){
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        String maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue)
                .replace("{" + MAX_ATTRIBUTE + "}", maxValue);
    }
}
