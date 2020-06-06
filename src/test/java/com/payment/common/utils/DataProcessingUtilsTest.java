package com.payment.common.utils;

import com.payment.common.code.RequestPayType;
import com.payment.model.PayReqInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class DataProcessingUtilsTest {

    @Test
    public void processingDataUtilTest(){
        String encryptedTestCardData =StringUtils.rightPad("TESTT",300,"*");
        PayReqInfo payReqInfo = PayReqInfo.builder()
                .payType(RequestPayType.PAYMENT)
                .payAmount(299_900)
                .cardNum("0123456789")
                .paymentId("20060523300000000001")
                .cvc("222")
                .period("2312")
                .planMonth("12")
                .vat(0)
                .originPaymentId(null)
                .encryptedCardData(encryptedTestCardData)
                .build();

        String processedData = DataProcessingUtils.processPayRequestData(payReqInfo);
        assertEquals(450,processedData.length());

        // 가공된 length 450의 데이터 중 , encryptedCardData를 추출
        assertEquals(encryptedTestCardData,DataProcessingUtils.getEncrytedCardDataFromProcessedData(processedData));
    }


}
