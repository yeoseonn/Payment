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
POST /api/payment

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


### 2. 취소 API (전체, 부분 취소)
PUT /api/cancel

API 기능 : 부가가치세 정보를 넘기지 않는 경우, 결제데이터의 부가가치세 금액으로 취소합니다
전체 취소/ 부분 취소 기능을 제공합니다. 

```
Request Param format : json
ex)
{
    "paymentId": "20060622470000000001",
    "payAmount": 10000,
    "vat": 1000
}

Response format : 
{
    "resultType": "SUCCESS",
    "payType": "PARTIAL_CANCEL",
    "paymentId": "20060622580000000002",
    "remainPayAmount": 10000,
    "remainVat": 909
}
```

* paymentId 는 관리번호로, 최초 결제시 부여받은 관리번호 입니다 .
* pay_cur_info에는 해당관리번호에 잔여하는? 존재하는 남은금액, 남은 vat를 저장합니다. 
* pay_req_info에는 취소 요청에 대한 고유번호를 생성하고, 기존 관리번호(결제 고유 번호) , 기존관리번호의 processed data를 저장합니다. 
* response에는 취소 요청에 대한 고유번호, 취소의 분류(전체 or 부분),잔여 금액 및 vat 를 포함합니다.
* com.payment.service.PaymentService.checkValidCancelRequest 메서드 내에서 모든 취소 가능 여부를 확인하며
throw하는 Exception은 Custom Exception으로, GlobalExceptionHandler를 구현하여 처리합니다. 
* pay_cur_info에 현재상태가 PAYMENT이고(취소를 한적이 없는 상태), 결제금액이 취소금액과 같으면 전체 취소입니다.



* 선택문제의 TestCase1,2,3 은 PaymentApiControllerTest의 testCase01,02,03에 구현하였습니다. 

### 3. 조회 API 
GET /api/{paymentId}

API 기능 : DB에 저장된 데이터를 조회해서 응답값으로 만들어줍니다
(pay_req_info에서 조회합니다.)

```
Response format : 
{
    "paymentId": "20060710450000000001",
    "card": {
        "cardNum": "123456*******567",
        "period": "2312",
        "cvc": "222"
    },
    "payType": "PAYMENT",
    "payAmount": 20000,
    "vat": 1818,
    "approvalDate": "2020-06-07 10:45:16.544"
}

```

### 4.멀티쓰레드 전략 


## 빌드 및 실행 방법
  Maven Build 
  
  mvn clean compile package 
  결과물 : paymentAPI.jar
