package com.payment.common.model;


import com.payment.common.code.PayType;
import com.payment.common.code.RequestPayType;
import com.payment.model.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PayInfoResponse {
    private String paymentId;
    private Card card;
    private RequestPayType payType;
    private int payAmount;
    private int vat;
    private String approvalDate;
}
