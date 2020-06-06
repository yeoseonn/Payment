# Payment API Project


## 개발 프레임워크
Spring Boot 2.3.0
H2
Java 1.8 
Mybatis 

## 테이블 설계
테이블 스키마 : resources/schema.sql 

pay_req_info  : 모든 결제 요청 정보를 저장 (결제, 전체취소, 부분취소)

pay_cur_info : 최종 결제 결과 정보를 저장

## 문제해결 전략

### 1. 결제 API 
URI : /api/payment
API 기능 : 카드정보과 금액정보를 입력받아서 카드사와 협의된 string 데이터로 DB에 저장합니다.

```
Request Param format : json
ex)
{
  "vat": 100,
  "payAmount": 299900,
  "planMonth": "12",
  "cardNum": "0123456789",
  "period": "2312",
  "cvc": "222"
}

```

* PaymentId는 Unique 값으로 com.payment.common.utils.PaymentIdGenerator에서 생성합니다. 
(pay_req_info 의 auto_increment key 에 현재날짜 'yyMMddHHss" 를 더한값)

* 카드정보의 암복호화는 com.payment.common.utils.CryptoUtils 을 사용합니다. 
카드정보의 구분자는 '/' 로 , 카드번호/유효기간/cvc 순으로 작성하여 암호화합니다.

* 시스템 정책 String Data 는 com.payment.common.utils.DataProcessingUtils 을 사용합니다. pay_req_info의 processed_data 칼럼에 저장합니다. 

* 입력정보의 길이 제한은 Spring의 Validation을 사용하고, db column의 사이즈로도 제한합니다. 


### 2. 취소 API 
/api/cancel



### 3.멀티쓰레드 전략 


## 빌드 및 실행 방법
  Maven Build 
  
  mvn clean compile package 
  결과물 : paymentAPI.jar
