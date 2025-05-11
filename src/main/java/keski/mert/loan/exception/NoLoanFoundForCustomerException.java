package keski.mert.loan.exception;

import keski.mert.loan.dto.ErrorType;

public class NoLoanFoundForCustomerException extends ApiException {

    private static final ErrorType errorType = ErrorType.NO_LOAN_FOUND_FOR_CUSTOMER;

    public NoLoanFoundForCustomerException() {
        super(errorType.getCode(), errorType.getMessage(), errorType.getHttpStatus());
    }

}
