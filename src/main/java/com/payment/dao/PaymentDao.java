package com.payment.dao;

import com.payment.model.PayCurInfo;
import com.payment.model.PayReqInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDao {
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    public int insertPayReqInfo(PayReqInfo payReqInfo) {
        sqlSessionTemplate.insert("insertPayReqInfo", payReqInfo);
        return payReqInfo.getId();
    }

    public int updatePayReqInfo(PayReqInfo payReqInfo) {
        return sqlSessionTemplate.update("updatePayReqInfo", payReqInfo);
    }

    public int insertPayCurInfo(PayReqInfo payReqInfo) {
        return sqlSessionTemplate.insert("insertPayCurInfo", payReqInfo);
    }

    public PayCurInfo selectPayCurInfo(String paymentId) {
        return sqlSessionTemplate.selectOne("selectPayCurInfo", paymentId);
    }

    public int updatePayCurInfo(PayCurInfo payCurInfo) {
        return sqlSessionTemplate.update("updatePayCurInfo", payCurInfo);
    }

    public PayReqInfo selectPayReqInfoByPaymentId(String paymentId) {
        return sqlSessionTemplate.selectOne("selectPayReqInfoByPaymentId", paymentId);
    }
}
