package com.study.SpringSecurity.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

public class ValidException extends RuntimeException {

    @Getter
    private List<FieldError> fieldErrors;

    public ValidException(String message, List<FieldError> fieldErrors) {
        super(message); // 부모 객체의 기본 메세지 출력 ( 유효성 검사 오류 메세지가 나타남 )
        this.fieldErrors = fieldErrors;
    }
}
