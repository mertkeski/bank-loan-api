package keski.mert.loan.exception;

import keski.mert.loan.dto.ErrorType;

public class InsufficientCreditLimitException extends ApiException {

    private static final ErrorType errorType = ErrorType.INSUFFICIENT_CREDIT_LIMIT;

    public InsufficientCreditLimitException() {
        super(errorType.getCode(), errorType.getMessage(), errorType.getHttpStatus());
    }

    public InsufficientCreditLimitException(String message) {
        super(errorType.getCode(), message, errorType.getHttpStatus());
    }
}
