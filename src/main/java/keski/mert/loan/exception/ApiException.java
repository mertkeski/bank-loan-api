package keski.mert.loan.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {

    private final int code;
    private final HttpStatus httpStatus;

    protected ApiException(int code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
