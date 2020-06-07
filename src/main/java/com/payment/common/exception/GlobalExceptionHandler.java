package com.payment.common.exception;

import com.payment.common.code.ErrorCode;
import com.payment.common.model.BasicErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = IllegalRequestException.class)
    @ResponseBody
    public ResponseEntity<BasicErrorResponse> handleIllegalRequestException(IllegalRequestException e) {
        log.error(e.getMessage(), e);
        BasicErrorResponse errorResponse = new BasicErrorResponse(e.getErrorCode().getErrorType(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage());
        log.error(errorMessage, ex);
        return new ResponseEntity<>(new BasicErrorResponse(ErrorCode.VALIDATION_ERROR.getErrorType(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<BasicErrorResponse> defaultHanlder(IllegalRequestException e) {
        log.error(e.getMessage(), e);
        BasicErrorResponse errorResponse = new BasicErrorResponse(e.getErrorCode().getErrorType(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
