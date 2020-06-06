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

        PayReqInfo payReqInfo = new PayReqInfo();
        payReqInfo.setPayType(RequestPayType.PAYMENT);
        payReqInfo.setPaymentId("20060523300000000001");
        payReqInfo.setCardNum("1234567890");
        payReqInfo.setPlanMonth("3");
        payReqInfo.setCvc("232");
        payReqInfo.setPeriod("2302");
        payReqInfo.setPayAmount(29_900);
        payReqInfo.setVat(0);
        payReqInfo.setOriginPaymentId(null);
        payReqInfo.setEncryptedCardData(encryptedTestCardData);

        String processedData = DataProcessingUtils.processPayRequestData(payReqInfo);
        assertEquals(450,processedData.length());

        // 가공된 length 450의 데이터 중 , encryptedCardData를 추출
        assertEquals(encryptedTestCardData,DataProcessingUtils.getEncrytedCardDataFromProcessedData(processedData));
    }


}
