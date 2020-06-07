package com.payment.controller;

import com.payment.common.model.CancelResponse;
import com.payment.common.model.PayInfoResponse;
import com.payment.common.model.PaymentResponse;
import com.payment.model.PayCancelReq;
import com.payment.model.PayReqInfo;
import com.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping(value = "/api/cancel",headers = "Accept=application/json")
    public CancelResponse cancel(@Valid @RequestBody PayCancelReq cancelReq){
        return paymentService.cancel(cancelReq);
    }

    @GetMapping(value = "/api/{paymentId}")
    public PayInfoResponse getPaymentInfo(@PathVariable String paymentId){
        return paymentService.getPaymentInfo(paymentId);
    }


}
