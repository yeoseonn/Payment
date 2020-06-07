package com.payment.service;

import com.payment.common.code.ErrorCode;
import com.payment.common.code.PayType;
import com.payment.common.code.RequestPayType;
import com.payment.common.code.ResultType;
import com.payment.common.exception.IllegalRequestException;
import com.payment.common.model.CancelResponse;
import com.payment.common.model.PayInfoResponse;
import com.payment.common.model.PaymentResponse;
import com.payment.common.utils.CryptoUtils;
import com.payment.common.utils.DataProcessingUtils;
import com.payment.common.utils.PaymentIdGenerator;
import com.payment.common.utils.VatCalUtils;
import com.payment.dao.PaymentDao;
import com.payment.model.Card;
import com.payment.model.PayCancelReq;
import com.payment.model.PayCurInfo;
import com.payment.model.PayReqInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {
    private final static String CARD_SPLIT_WORD = "/";

    @Autowired
    private PaymentDao paymentDao;


    /**
     * 결제 요청을 받아 처리
     *
     * @param paramPayReq
     * @return
     */
    public PaymentResponse payment(PayReqInfo paramPayReq) {
        PayReqInfo payReqInfo = checkVatAndPayAmount(paramPayReq);
        payReqInfo.setPayType(RequestPayType.PAYMENT);

        //autoIncrement Id 생성하여 payment unique Id 생성
        int id = paymentDao.insertPayReqInfo(payReqInfo);
        payReqInfo.setId(id);
        String paymentId = PaymentIdGenerator.generatePaymentId(payReqInfo.getId());
        payReqInfo.setPaymentId(paymentId);

        payReqInfo.setEncryptedCardData(getEncryptedCardData(payReqInfo));
        payReqInfo.setProcessedData(DataProcessingUtils.processPayRequestData(payReqInfo));

        //update paymentId
        paymentDao.updatePayReqInfo(payReqInfo);
        paymentDao.insertPayCurInfo(payReqInfo);
        return new PaymentResponse(ResultType.SUCCESS, paymentId);
    }

    /**
     * 요청이 유효한지 검사
     * 1. vat가 0 인 경우
     * 2. 결제 금액 최소 100,최대 100_000_000)
     *
     * @param paramPayReq
     */
    private PayReqInfo checkVatAndPayAmount(PayReqInfo paramPayReq) {
        PayReqInfo payReqInfo = paramPayReq;
        if (payReqInfo.getVat() == null) {
            payReqInfo.setVat(VatCalUtils.getVatWithoutOption(payReqInfo.getPayAmount()));
        } else if (payReqInfo.getPayAmount() < payReqInfo.getVat()) {
            throw new IllegalRequestException(ErrorCode.VALIDATION_ERROR, "VAT is larger than Pay Amount");
        }

        if (paramPayReq.getPayAmount() < 100) {
            throw new IllegalRequestException(ErrorCode.VALIDATION_ERROR, "Pay Amount must be larger than 100");
        }

        if (paramPayReq.getPayAmount() > 100_000_000) {
            throw new IllegalRequestException(ErrorCode.VALIDATION_ERROR, "Pay Amount must be smaller than 100,000,000");
        }

        return payReqInfo;
    }

    /**
     * 암호화 전 구분자 추가 & 암호화
     * 카드번호 / 유효기간 / CVC
     *
     * @param payReqInfo
     * @return 암호화 된 카드 정보들,,
     */
    private String getEncryptedCardData(PayReqInfo payReqInfo) {
        String cardDataToEncrypt = new StringBuilder()
                .append(payReqInfo.getCardNum())
                .append(CARD_SPLIT_WORD)
                .append(payReqInfo.getPeriod())
                .append(CARD_SPLIT_WORD)
                .append(payReqInfo.getCvc())
                .toString();

        return CryptoUtils.encrypt(cardDataToEncrypt);
    }

    /**
     * 암호화 된 Card정보를 복호화 & 구분자로 Parse하고
     * Card model로 return
     *
     * @param encryptedData
     * @return
     */
    private Card getDecryptedCardData(String encryptedData) {
        String decrypteData = CryptoUtils.decrypt(encryptedData);
        String[] cardTokens = decrypteData.split(CARD_SPLIT_WORD);

        return new Card(cardTokens[0], cardTokens[1], cardTokens[2]);
    }


    /**
     * 결제 취소 요청
     *
     * @param paramReq
     * @return
     */
    public CancelResponse cancel(PayCancelReq paramReq) {
        if (paramReq.getVat() == null) {
            paramReq.setVat(VatCalUtils.getVatWithoutOption(paramReq.getPayAmount()));
        }
        PayCurInfo payCurInfo = paymentDao.selectPayCurInfo(paramReq.getPaymentId());
        checkValidCancelRequest(paramReq, payCurInfo);


        //거래 요청 데이터 생성 및 paymentId, processedData 업데이트
        PayReqInfo payReqInfo = new PayReqInfo(paramReq);
        int id = paymentDao.insertPayReqInfo(payReqInfo);
        payReqInfo.setPaymentId(PaymentIdGenerator.generatePaymentId(id));
        payReqInfo.setProcessedData(payCurInfo.getProcessedData());
        paymentDao.updatePayReqInfo(payReqInfo);

        PayType requestPayType = PayType.PARTIAL_CANCEL;
        if (payCurInfo.getPayType() == PayType.PAYMENT && payCurInfo.getCurPayAmount() == paramReq.getPayAmount()) { // 전체 취소의 조건
            requestPayType = PayType.ALL_CANCEL;
        }
        payCurInfo.setCurPayAmount(payCurInfo.getCurPayAmount() - paramReq.getPayAmount());
        payCurInfo.setCurVat(payCurInfo.getCurVat() - paramReq.getVat());
        payCurInfo.setPayType(requestPayType);
        paymentDao.updatePayCurInfo(payCurInfo);

        return new CancelResponse(ResultType.SUCCESS, requestPayType, payReqInfo.getPaymentId(), payCurInfo.getCurPayAmount(), payCurInfo.getCurVat());
    }

    /**
     * 취소 요청 금액이 유효한지 확인
     *
     * @param payCancelReq
     * @param payCurInfo
     */
    public void checkValidCancelRequest(PayCancelReq payCancelReq, PayCurInfo payCurInfo) {
        if (payCurInfo.getCurPayAmount() == 0) {
            throw new IllegalRequestException(ErrorCode.CANCEL_NOT_AVALIABLE, "Existing Pay Amount is 0");
        }

        if (payCurInfo.getCurPayAmount() < payCancelReq.getPayAmount()) {
            throw new IllegalRequestException(ErrorCode.CANCEL_NOT_AVALIABLE, "Cancel Pay Amount cannot be larger than Existing Pay Amount");
        }

        if (payCurInfo.getCurVat() < payCancelReq.getVat()) {
            throw new IllegalRequestException(ErrorCode.CANCEL_NOT_AVALIABLE, "Cancel Vat cannot be larger than Existing Vat");
        }

        if (payCurInfo.getCurPayAmount() == payCancelReq.getPayAmount() && payCurInfo.getCurVat() > payCancelReq.getVat()) {
            throw new IllegalRequestException(ErrorCode.CANCEL_NOT_AVALIABLE, "total vat is remain!");
        }
    }

    public PayInfoResponse getPaymentInfo(String paymentId) {
        if (paymentId.length() != 20) {
            throw new IllegalRequestException(ErrorCode.VALIDATION_ERROR, "Payment Id length must be 20");
        }

        PayReqInfo payReqInfo = paymentDao.selectPayReqInfoByPaymentId(paymentId);
        if (payReqInfo == null) {
            throw new IllegalRequestException(ErrorCode.INTERNAL_SERVER_ERROR, "Cannot Find Payment Info");
        }

        String cardData = DataProcessingUtils.getEncrytedCardDataFromProcessedData(payReqInfo.getProcessedData());
        Card card = getDecryptedCardData(cardData);

        return new PayInfoResponse(paymentId, card, payReqInfo.getPayType(), payReqInfo.getPayAmount(), payReqInfo.getVat(), payReqInfo.getCreated());

    }
}
