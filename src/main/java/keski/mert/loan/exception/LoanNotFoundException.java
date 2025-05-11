package keski.mert.loan.exception;

import keski.mert.loan.dto.ErrorType;

public class LoanNotFoundException extends ApiException {

    private static final ErrorType errorType = ErrorType.LOAN_NOT_FOUND;

    public LoanNotFoundException() {
        super(errorType.getCode(), errorType.getMessage(), errorType.getHttpStatus());
    }

}
