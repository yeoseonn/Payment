package com.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.PaymentApplication;
import com.payment.common.model.BasicErrorResponse;
import com.payment.common.model.CancelResponse;
import com.payment.common.model.PaymentResponse;
import com.payment.model.PayCancelReq;
import com.payment.model.PayReqInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = PaymentApplication.class)
@WebAppConfiguration
@Slf4j
public class PaymentApiControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    public ResultActions callPaymentAPI(PayReqInfo payReqInfo) throws Exception {
        String requestString = objectMapper.writeValueAsString(payReqInfo);
        return mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.toString())
                .content(requestString));
    }

    public ResultActions callCancelAPI(PayCancelReq payCancelReq) throws Exception {
        String requestString = objectMapper.writeValueAsString(payCancelReq);
        return mockMvc.perform(put("/api/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.toString())
                .content(requestString));
    }

    @Test
    public void paymentTest() throws Exception {
        PayReqInfo payReqInfo = PayReqInfo.builder()
                .payAmount(299_900)
                .cardNum("0123456789")
                .cvc("222")
                .period("2312")
                .planMonth("12")
                .build();

        MvcResult mvcResult = callPaymentAPI(payReqInfo).andReturn();
        String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        String idWithPad = StringUtils.leftPad("1", 10, "0");

        String returnVal = mvcResult.getResponse().getContentAsString();
        PaymentResponse paymentResponse = objectMapper.readValue(returnVal, PaymentResponse.class);
        assertEquals(nowDateTime + idWithPad, paymentResponse.getPaymentId());
    }

    /**
     * 실패 test
     *
     * @throws Exception
     */
    @Test
    public void paymentFailTest() throws Exception {
        PayReqInfo payReqInfo = PayReqInfo.builder()
                .payAmount(299_900)
                .vat(300_000)
                .cardNum("0123456789")
                .cvc("222")
                .period("2312")
                .planMonth("12")
                .build();

        callPaymentAPI(payReqInfo).andDo(print()).andExpect(status().is5xxServerError());
    }

    @Test
    public void testCase01() throws Exception {
        PayReqInfo payReqInfo = PayReqInfo.builder()
                .payAmount(11_000)
                .vat(1000)
                .cardNum("0123456789")
                .cvc("222")
                .period("2312")
                .planMonth("12")
                .build();
        MvcResult mvcResult = callPaymentAPI(payReqInfo).andReturn();
        PaymentResponse paymentResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PaymentResponse.class);
        String paymentId = paymentResponse.getPaymentId();
        log.info("11000원 결제 요청 관리 번호 : " + paymentId);


        PayCancelReq payCancelReq = new PayCancelReq(paymentId, 1100, 100);
        mvcResult = callCancelAPI(payCancelReq).andReturn();
        CancelResponse cancelResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CancelResponse.class);
        log.info("1100/100 취소 요청 status : " + cancelResponse.getResultType());
        assertEquals(9900, cancelResponse.getRemainPayAmount());
        assertEquals(900, cancelResponse.getRemainVat());


        payCancelReq = new PayCancelReq(paymentId, 3300);
        mvcResult = callCancelAPI(payCancelReq).andReturn();
        cancelResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CancelResponse.class);
        log.info("3300/null 취소 요청 status : " + cancelResponse.getResultType());
        assertEquals(6600, cancelResponse.getRemainPayAmount());
        assertEquals(600, cancelResponse.getRemainVat());

        payCancelReq = new PayCancelReq(paymentId, 7000);
        mvcResult = callCancelAPI(payCancelReq).andDo(print()).andExpect(status().is5xxServerError()).andReturn();
        BasicErrorResponse basicErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BasicErrorResponse.class);
        log.info("700/null 취소 요청 실패 메세지 : " + basicErrorResponse.getMessage());

        payCancelReq = new PayCancelReq(paymentId, 6600, 700);
        mvcResult = callCancelAPI(payCancelReq).andDo(print()).andExpect(status().is5xxServerError()).andReturn();
        basicErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BasicErrorResponse.class);
        log.info("6600/700 취소 요청 실패 메세지 : " + basicErrorResponse.getMessage());

        payCancelReq = new PayCancelReq(paymentId, 6600,600);
        mvcResult = callCancelAPI(payCancelReq).andReturn();
        cancelResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CancelResponse.class);
        log.info("6600/600 취소 요청 status : " + cancelResponse.getResultType());
        assertEquals(0, cancelResponse.getRemainPayAmount());
        assertEquals(0, cancelResponse.getRemainVat());

        payCancelReq = new PayCancelReq(paymentId, 100);
        mvcResult = callCancelAPI(payCancelReq).andDo(print()).andExpect(status().is5xxServerError()).andReturn();
        basicErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BasicErrorResponse.class);
        log.info("100/null 취소 요청 실패 메세지 : " + basicErrorResponse.getMessage());
    }

    @Test
    public void testCase02() throws Exception {
        PayReqInfo payReqInfo = PayReqInfo.builder()
                .payAmount(20_000)
                .vat(909)
                .cardNum("0123456789")
                .cvc("222")
                .period("2312")
                .planMonth("12")
                .build();
        MvcResult mvcResult = callPaymentAPI(payReqInfo).andReturn();
        PaymentResponse paymentResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PaymentResponse.class);
        String paymentId = paymentResponse.getPaymentId();
        log.info("20000 결제 요청 관리 번호 : " + paymentId);


        PayCancelReq payCancelReq = new PayCancelReq(paymentId, 10_000, 0);
        mvcResult = callCancelAPI(payCancelReq).andReturn();
        CancelResponse cancelResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CancelResponse.class);
        log.info("10000/0 취소 요청 status : " + cancelResponse.getResultType());
        assertEquals(10_000, cancelResponse.getRemainPayAmount());
        assertEquals(909, cancelResponse.getRemainVat());

        payCancelReq = new PayCancelReq(paymentId, 10_000,0);
        mvcResult = callCancelAPI(payCancelReq).andDo(print()).andExpect(status().is5xxServerError()).andReturn();
        BasicErrorResponse basicErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BasicErrorResponse.class);
        log.info("10000/0 취소 요청 실패 메세지 : " + basicErrorResponse.getMessage());

        payCancelReq = new PayCancelReq(paymentId, 10_000,909);
        mvcResult = callCancelAPI(payCancelReq).andReturn();
        cancelResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CancelResponse.class);
        log.info("10000/909 취소 요청 status : " + cancelResponse.getResultType());
        assertEquals(0, cancelResponse.getRemainPayAmount());
        assertEquals(0, cancelResponse.getRemainVat());
    }


    @Test
    public void testCase03() throws Exception {
        PayReqInfo payReqInfo = PayReqInfo.builder()
                .payAmount(20_000)
                .vat(null)
                .cardNum("0123456789")
                .cvc("222")
                .period("2312")
                .planMonth("12")
                .build();
        MvcResult mvcResult = callPaymentAPI(payReqInfo).andReturn();
        PaymentResponse paymentResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PaymentResponse.class);
        String paymentId = paymentResponse.getPaymentId();
        log.info("20000 결제 요청 관리 번호 : " + paymentId);


        PayCancelReq payCancelReq = new PayCancelReq(paymentId, 10_000, 1_000);
        mvcResult = callCancelAPI(payCancelReq).andReturn();
        CancelResponse cancelResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CancelResponse.class);
        log.info("10000/0 취소 요청 status : " + cancelResponse.getResultType());
        assertEquals(10_000, cancelResponse.getRemainPayAmount());
        assertEquals(818, cancelResponse.getRemainVat());

        payCancelReq = new PayCancelReq(paymentId, 10_000,909);
        mvcResult = callCancelAPI(payCancelReq).andDo(print()).andExpect(status().is5xxServerError()).andReturn();
        BasicErrorResponse basicErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BasicErrorResponse.class);
        log.info("10000/909 취소 요청 실패 메세지 : " + basicErrorResponse.getMessage());

        payCancelReq = new PayCancelReq(paymentId, 10_000,null);
        mvcResult = callCancelAPI(payCancelReq).andReturn();
        cancelResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CancelResponse.class);
        log.info("10000/909 취소 요청 status : " + cancelResponse.getResultType());
        assertEquals(0, cancelResponse.getRemainPayAmount());
        assertEquals(0, cancelResponse.getRemainVat());
    }


}
