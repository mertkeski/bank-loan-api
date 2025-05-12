package keski.mert.loan.exception;

import keski.mert.loan.dto.ErrorType;

public class NotAuthorizedException extends ApiException {

    private static final ErrorType errorType = ErrorType.NOT_AUTHORIZED;

    public NotAuthorizedException() {
        super(errorType.getCode(), errorType.getMessage(), errorType.getHttpStatus());
    }

}
