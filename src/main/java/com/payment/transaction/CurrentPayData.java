package com.payment.transaction;

import com.payment.model.PayCancelReq;
import com.payment.model.PayReqInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrentPayData {
    public static Map<String, PayReqInfo> cardDataMap;
    public static Map<String, PayCancelReq> paymentIdMap;

    static {
        cardDataMap = new ConcurrentHashMap<>();
        paymentIdMap = new ConcurrentHashMap<>();
    }
}
