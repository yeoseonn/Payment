package com.payment.common.model;

import com.payment.common.code.ResultType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private ResultType resultType;
    private String paymentId;
    private String created;

    public PaymentResponse(ResultType resultType, String paymentId) {
        this.resultType = resultType;
        this.paymentId = paymentId;
    }
}
