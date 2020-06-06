package com.payment.common.code;

import org.apache.commons.lang3.StringUtils;

public enum PayType {
    PAYMENT("Payment"),
    ALL_CANCEL("All_Cancel"),
    PARTIAL_CALCEL("Parital_Cancel");

    private String payType;

    PayType(String payType) {
        this.payType = payType;
    }

    public String getPayType() {
        return payType;
    }

    public static PayType getEnumByPayType(String payType) {
        for (PayType payTypeEnum : PayType.values()) {
            if (StringUtils.equals(payTypeEnum.getPayType(), payType)) {
                return payTypeEnum;
            }
        }
        return null;
    }
}
