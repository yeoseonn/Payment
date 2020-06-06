package com.payment.dao;

import com.payment.model.PayReqInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDao {
    @Autowired
    SqlSession sqlSession;

    public int insertPayReqInfo (PayReqInfo payReqInfo){
        sqlSession.insert("insertPayReqInfo",payReqInfo);
        return payReqInfo.getId();
    }

    public int updatePaymentIdReq(PayReqInfo payReqInfo){
        return sqlSession.update("updatePaymentIdReq",payReqInfo);
    }


}
