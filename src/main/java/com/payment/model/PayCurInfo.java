package com.payment.model;

import com.payment.common.code.PayType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class PayCurInfo {
    private int id;

    /**
     * 거래 관리 번호 (unique id)
     */
    @Length(min = 20, max = 20)
    private String paymentId;

    /**
     * 현재 거래 상태
     * PAYMENT(승인), ALL_CANCEL(전체 취소), PARTIAL_CANCEL(부분 취소)
     */
    private PayType payType;


    /**
     *  부가가치세 (최종 남아있는? 부가가치세)
     */
    private int curVat;

    /**
     * 최종 금액
     */
    @Length(min = 100, max = 100_000_000)
    private int curPayAmount;


    /**
     * 할부 금액 0 : 일시불, 1~12
     */
    @Length(max = 2)
    private String planMonth;

    /**
     * Updated Date
     */
    private String updated;

}
