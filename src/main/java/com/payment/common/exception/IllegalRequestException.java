package com.payment.common.exception;

import com.payment.common.code.ErrorCode;

public class IllegalRequestException extends RuntimeException {
    private final ErrorCode errorCode;

    public IllegalRequestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public IllegalRequestException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

