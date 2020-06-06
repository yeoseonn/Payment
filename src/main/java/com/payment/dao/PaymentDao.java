package com.payment.dao;

import com.payment.model.PayReqInfo;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDao {
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    public int insertPayReqInfo (PayReqInfo payReqInfo){
        sqlSessionTemplate.insert("insertPayReqInfo",payReqInfo);
        return payReqInfo.getId();
    }

    public int updatePaymentIdReq(PayReqInfo payReqInfo){
        return sqlSessionTemplate.update("updatePaymentIdReq",payReqInfo);
    }

    public int insertPayCurInfo(PayReqInfo payReqInfo){
        return sqlSessionTemplate.insert("insertPayCurInfo",payReqInfo);
    }


}
