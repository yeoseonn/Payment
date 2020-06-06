package com.payment.common.model;

import com.payment.common.code.ResultType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponse {
    private ResultType resultType;
    private String paymentId;
    private String created;
}
