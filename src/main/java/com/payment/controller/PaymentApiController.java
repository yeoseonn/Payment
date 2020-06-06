package com.payment.controller;

import com.payment.common.model.PaymentResponse;
import com.payment.model.PayReqInfo;
import com.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class PaymentApiController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/api/payment", headers = "Accept=application/json")
    public PaymentResponse payment(@Valid @RequestBody PayReqInfo payReqInfo) {
        return paymentService.payment(payReqInfo);
    }


}
