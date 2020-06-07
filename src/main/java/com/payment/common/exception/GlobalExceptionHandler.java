package com.payment.common.exception;

import com.payment.common.code.ErrorCode;
import com.payment.common.model.BasicErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = IllegalRequestException.class)
    @ResponseBody
    public ResponseEntity<BasicErrorResponse> handleIllegalArgumentException(IllegalRequestException e){
        log.error(e.getMessage(),e);
        BasicErrorResponse errorResponse = new BasicErrorResponse(e.getErrorCode().getErrorType(),e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public BasicErrorResponse handleValidationCheckException(ConstraintViolationException exception) {
        log.error("Valiadation Fail ", getResultMessage(exception.getConstraintViolations().iterator()));
        return new BasicErrorResponse(ErrorCode.VALIDATION_ERROR);
    }

    private String getResultMessage(Iterator<ConstraintViolation<?>> violationIterator){
        StringBuilder resultMessageBuilder = new StringBuilder();
        while (violationIterator.hasNext()) {
            ConstraintViolation<?> constraintViolation = violationIterator.next();
            resultMessageBuilder
                    .append("['")
                    .append(getPopertyName(constraintViolation.getPropertyPath().toString())) // 유효성 검사가 실패한 속성
                    .append("' is '")
                    .append(constraintViolation.getInvalidValue()) // 유효하지 않은 값
                    .append("'. ")
                    .append(constraintViolation.getMessage()) // 유효성 검사 실패 시 메시지
                    .append("]");

            if (violationIterator.hasNext() == true) {
                resultMessageBuilder.append(", ");
            }
        }

        return resultMessageBuilder.toString();
    }

    protected String getPopertyName(String propertyPath) {
        return propertyPath.substring(propertyPath.lastIndexOf('.') + 1); // 전체 속성 경로에서 속성 이름만 가져온다.
    }
}
