package keski.mert.loan.exception;

import keski.mert.loan.dto.ErrorType;

public class NoLoanFoundException extends ApiException {

    private static final ErrorType errorType = ErrorType.NO_LOAN_FOUND;

    public NoLoanFoundException() {
        super(errorType.getCode(), errorType.getMessage(), errorType.getHttpStatus());
    }

}
