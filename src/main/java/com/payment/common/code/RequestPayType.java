package com.payment.common.code;

import org.apache.commons.lang3.StringUtils;

public enum RequestPayType {
    PAYMENT("Payment"),
    CANCEL("Cancel");

    private String type;

    RequestPayType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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
