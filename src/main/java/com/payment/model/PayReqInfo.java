package com.payment.model;

import com.payment.common.code.PayType;
import com.payment.common.code.RequestPayType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayReqInfo {
    private int id;

    /**
     * 거래 관리 번호 (unique id)
     */
    @Length(min = 20, max = 20)
    private String paymentId;


    /**
     * 취소시 원래 거래 관리 번호 (unique id)
     */
    @Length(min = 20, max = 20)
    private String originPaymentId;


    /**
     * PAYMENT(승인), CANCEL(취소)
     */
    private RequestPayType payType;

    /**
     *  부가가치세 or 취소 부가가치세 (optional)
     */
    private Integer vat;

    /**
     * 결제 금액 or 취소 금액
     */
    @NotNull
    private int payAmount;

    /**
     * 할부 금액 0 : 일시불, 1~12
     */
    @Length(max = 2)
    @NotNull
    private String planMonth;


    /**
     *  암호화 된 카드 Data
     */
    @Length(max=300)

    private String encryptedCardData;

    /**
     * Card Num
     */
    @Length(min = 10, max = 16)
    @NotNull
    private String cardNum;

    /**
     * Card Period
     */
    @Length(min = 4, max = 4)
    @NotNull
    private String period;

    /**
     * Card cvc
     */
    @Length(min = 3, max = 3)
    @NotNull
    private String cvc;

    private String created;

    /**
     * 거래 데이터 명세
     */
    @Length(max=450)
    private String processedData;

    public PayReqInfo(PayCancelReq payCancelReq){
        this.originPaymentId = payCancelReq.getPaymentId();
        this.payAmount = payCancelReq.getPayAmount();
        this.vat = payCancelReq.getVat();
        this.payType = payType;
        this.payType = RequestPayType.CANCEL;
        this.planMonth  = "00";
    }

}
