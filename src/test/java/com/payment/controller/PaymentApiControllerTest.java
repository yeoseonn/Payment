package com.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.PaymentApplication;
import com.payment.common.code.RequestPayType;
import com.payment.common.model.PaymentResponse;
import com.payment.model.PayReqInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = PaymentApplication.class)
@WebAppConfiguration
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

    @Test
    public void paymentTest() throws Exception {
        PayReqInfo payReqInfo = PayReqInfo.builder()
                .payType(RequestPayType.PAYMENT)
                .payAmount(299_900)
                .cardNum("0123456789")
                .cvc("222")
                .period("2312")
                .planMonth("12")
                .build();

        String requestString = objectMapper.writeValueAsString(payReqInfo);

        MvcResult mvcResult = mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.toString())
                .content(requestString)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        String idWithPad = StringUtils.leftPad("1", 10, "0");

        String returnVal = mvcResult.getResponse().getContentAsString();
        PaymentResponse paymentResponse  = objectMapper.readValue(returnVal, PaymentResponse.class);
        assertEquals(nowDateTime+idWithPad,paymentResponse.getPaymentId());
    }
}
