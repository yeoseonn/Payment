package com.payment.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentIdGenerator {
    private static final String datePattern = "yyMMddHHmm";

    public static String generatePaymentId(int id) {
        String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern));
        String idWithPad = StringUtils.leftPad(String.valueOf(id), 10, "0");
        return nowDateTime + idWithPad;
    }


}
