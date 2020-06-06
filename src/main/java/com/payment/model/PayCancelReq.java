package com.payment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayCancelReq {
    /**
     * 거래 관리 번호 (unique id)
     */
    @Length(min = 20, max = 20)
    @NotNull
    private String paymentId;

    /**
     * 결제 금액 or 취소 금액
     */
    @NotNull
    private int payAmount;

    private Integer vat;

    public PayCancelReq(String paymentId, int payAmount) {
        this.paymentId = paymentId;
        this.payAmount = payAmount;
    }
}
