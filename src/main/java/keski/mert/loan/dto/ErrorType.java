package keski.mert.loan.dto;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    CUSTOMER_NOT_FOUND(101, "Customer not found.", HttpStatus.NOT_FOUND),
    NO_LOAN_FOUND_FOR_CUSTOMER(102, "No loan found for customer.", HttpStatus.NOT_FOUND),
    LOAN_NOT_FOUND(103, "Loan not found.", HttpStatus.NOT_FOUND),
    INSUFFICIENT_CREDIT_LIMIT(121, "Customer does not have enough credit limit.", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(140, "Validation failed.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(150, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorType(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
