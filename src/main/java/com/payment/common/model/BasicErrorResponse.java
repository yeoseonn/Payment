package com.payment.common.model;

import com.payment.common.code.ErrorCode;
import lombok.Data;

@Data
public class BasicErrorResponse {
    private String code;
    private String message;

    public BasicErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getErrorType();
        this.message = errorCode.getMessage();
    }

    public BasicErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}