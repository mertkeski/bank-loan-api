package keski.mert.loan.exception;

import keski.mert.loan.dto.ErrorType;

public class CustomerNotFoundException extends ApiException {

    private static final ErrorType errorType = ErrorType.CUSTOMER_NOT_FOUND;

    public CustomerNotFoundException() {
        super(errorType.getCode(), errorType.getMessage(), errorType.getHttpStatus());
    }

}
