package com.payment.common.exception;

import com.payment.common.code.ErrorCode;

public class PaymentFailException extends RuntimeException {
    private final ErrorCode errorCode;

    public PaymentFailException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public PaymentFailException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

