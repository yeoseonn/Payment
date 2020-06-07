package com.payment.common.utils;

import com.payment.model.PayReqInfo;
import org.apache.commons.lang3.StringUtils;

public class DataProcessingUtils {
    private static final String STRING_PAD = "_";
    private static final String NUMBER_PAD = "0";
    private static final String MASKING = "*";

    /**
     * Spec에 명시되어있는 String Data 제공
     *
     * @param payReqInfo
     * @return
     */
    public static String processPayRequestData(PayReqInfo payReqInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append(StringUtils.rightPad(payReqInfo.getPayType().getType(), 10, STRING_PAD))
                .append(StringUtils.rightPad(payReqInfo.getPaymentId(), 20, STRING_PAD))
                .append(StringUtils.rightPad(payReqInfo.getCardNum(), 20, STRING_PAD))
                .append(StringUtils.leftPad(payReqInfo.getPlanMonth(), 2, NUMBER_PAD))
                .append(StringUtils.rightPad(payReqInfo.getPeriod(), 4, STRING_PAD))
                .append(StringUtils.rightPad(payReqInfo.getCvc(), 3, STRING_PAD))
                .append(StringUtils.leftPad(Integer.toString(payReqInfo.getPayAmount()), 10, STRING_PAD))
                .append(StringUtils.leftPad(Integer.toString(payReqInfo.getVat()), 10, NUMBER_PAD))
                .append(StringUtils.rightPad(StringUtils.defaultIfEmpty(payReqInfo.getOriginPaymentId(), STRING_PAD), 20, STRING_PAD))
                .append(StringUtils.rightPad(payReqInfo.getEncryptedCardData(), 300, STRING_PAD))
                .append(StringUtils.leftPad(STRING_PAD, 47, STRING_PAD));
        String resultRawData = builder.toString();
        int totalLength = resultRawData.length();
        return StringUtils.leftPad(String.valueOf(totalLength), 4, STRING_PAD) + resultRawData;
    }

    /**
     * Encrypted Card Data 분류 .
     * 450 length의  String중, (34~53 : 카드번호 , 54 ~ 55 : 할부개월, 56~ 59 : 카드 유효기간
     * , 60~62 ; 카드 cvc , 103~403 : encryptedCardData )
     *
     * @param processedData
     * @return
     */
    public static String getEncrytedCardDataFromProcessedData(String processedData) {
        String encryptedCardData = processedData.substring(103, 403);
        return encryptedCardData.substring(0, encryptedCardData.indexOf(STRING_PAD));
    }

    /**
     * 앞 6자리와 뒤 3자리를 제외한 나머지를 마스킹처리
     *
     * @return
     */
    public static String maskingCardNum(String cardNumParam) {
        String cardNum = cardNumParam;

        int length = cardNum.length();
        String masking = StringUtils.repeat(MASKING, length - 9);
        String str = cardNum.substring(0, 6) + masking + cardNum.substring(length - 3, length);
        return str;
    }
}
