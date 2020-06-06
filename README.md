# Payment API Project


## 개발 프레임워크
Spring Boot 2.3.0
H2
Java 1.8 
Mybatis 

##테이블 설계
테이블 스키마 : resources/schema.sql 

pay_req_info  : 모든 결제 요청 정보를 저장 (결제, 전체취소, 부분취소)

pay_cur_info : 최종 결제 결과 정보를 저장

##문제해결 전략
1. 결제 API 
/api/payment

API 기능 : 카드정보과 금액정보를 입력받아서 카드사와 협의된 string 데이터로 DB에 저장합니다.


취소 API 
/api/cancel



##빌드 및 실행 방법
  Maven Build 
  
  mvn clean compile package 
  결과물 : paymentAPI.jar