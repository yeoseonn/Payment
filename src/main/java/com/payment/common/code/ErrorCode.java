package com.payment.common.code;

public enum ErrorCode {
    INTERNAL_SERVER_ERROR("ERR_500", "Internal Server Error Occured"),
    CARD_INFO_ERROR("ERR_100", "Card Info is Wrong"),
    VALIDATION_ERROR("ERR_101","Validation Check Fail"),
    CANCEL_NOT_PAVALIABLE("ERR_200", "");

    private String errorType;
    private String message;

    ErrorCode(String errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getMessage() {
        return message;
    }
}