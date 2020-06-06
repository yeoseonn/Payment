package com.payment.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class VatCalUtilsTest {

    @Test
    public void calculateVat() {
        int payAmount = 1_000;
        int defaultVat = VatCalUtils.getVatWithoutOption(payAmount);

        assertEquals(91, defaultVat);
    }
}
