package com.payment.common.model;

import com.payment.common.code.PayType;
import com.payment.common.code.ResultType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelResponse {
    private ResultType resultType;
    private PayType payType;
    private String paymentId;
    private int remainPayAmount;
    private int remainVat;
}
