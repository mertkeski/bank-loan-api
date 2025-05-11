package keski.mert.loan.exception;

import keski.mert.loan.dto.ErrorResponse;
import keski.mert.loan.dto.ErrorType;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleBaseApiException(ApiException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorType errorType = ErrorType.INVALID_REQUEST;
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("Validation failed.");

        ErrorResponse errorResponse = new ErrorResponse(errorType.getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, errorType.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorType errorType = ErrorType.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(errorType.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, errorType.getHttpStatus());
    }

}
