package com.payment.transaction;


import com.payment.common.code.ErrorCode;
import com.payment.common.exception.IllegalRequestException;
import com.payment.model.PayCancelReq;
import com.payment.model.PayReqInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TransactionAspect {

    @Before("execution(* com.payment.service.PaymentService.payment(..))")
    public void paymentBefore(JoinPoint joinPoint) {
        log.info("-----------Before Payment-------------");
        PayReqInfo payReqInfo = (PayReqInfo) joinPoint.getArgs()[0];
        String cardNum = payReqInfo.getCardNum();
        if (CurrentPayData.cardDataMap.get(cardNum) == null) {
            log.info(cardNum + "is Request to Payment");
            CurrentPayData.cardDataMap.put(cardNum, payReqInfo);
        } else {
            log.error(cardNum + "is Using Now");
            throw new IllegalRequestException(ErrorCode.PAYMENT_NOT_AVAILABLE, cardNum + "is Using now");
        }

    }

    @After("execution(* com.payment.service.PaymentService.payment(..))")
    public void paymentAfter(JoinPoint joinPoint) {
        log.info("-----------After Payment-------------");
        PayReqInfo payReqInfo = (PayReqInfo) joinPoint.getArgs()[0];
        CurrentPayData.cardDataMap.remove(payReqInfo.getCardNum());
    }


    @Before("execution(* com.payment.service.PaymentService.cancel(..))")
    public void cancelBefore(JoinPoint joinPoint) {
        log.info("-----------Before Cacncel-------------");
        PayCancelReq payCancelReq = (PayCancelReq) joinPoint.getArgs()[0];
        if (CurrentPayData.paymentIdMap.get(payCancelReq.getPaymentId()) == null) {
            CurrentPayData.paymentIdMap.put(payCancelReq.getPaymentId(), payCancelReq);
        } else {
            throw new IllegalRequestException(ErrorCode.CANCEL_NOT_AVALIABLE, payCancelReq.getPaymentId() + " Cancel is now using");
        }
    }

    @After("execution(* com.payment.service.PaymentService.cancel(..))")
    public void cacncelAfter(JoinPoint joinPoint) {
        log.info("-----------After Cancel-------------");
        PayCancelReq payCancelReq = (PayCancelReq) joinPoint.getArgs()[0];
        CurrentPayData.paymentIdMap.remove(payCancelReq.getPaymentId());
    }

}
