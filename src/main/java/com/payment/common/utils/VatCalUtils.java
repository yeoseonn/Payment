package com.payment.common.utils;

public class VatCalUtils {
    private static final int DEFAULT_VAT_VAL = 11;

    public static int getVatWithoutOption(int payAmount) {
        return (int) Math.ceil((double) payAmount / DEFAULT_VAT_VAL);
    }
}
