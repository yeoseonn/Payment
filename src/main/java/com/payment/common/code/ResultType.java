package com.payment.common.code;

public enum ResultType {
    SUCCESS("success"),
    FAIL("fail");

    private String resultType;

    ResultType(String resultType) {
        this.resultType = resultType;
    }
}
