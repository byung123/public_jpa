package com.study.SpringSecurity.controller;

import com.study.SpringSecurity.exception.ValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

// Component기 때문에 IOC 컨테이너 안에서 발생한 오류만 낚아챌 수 있다. 그 외는 낚아채지 않는다. 예를 dto에서 오류가 나오면 그것을 가져오지 못함
@RestControllerAdvice
public class ExceptionControllerAdvice {

    // throw 한 걸 이녀석이 받음 (ValidException 형태의 객체만 받을 수 있음)
    @ExceptionHandler(ValidException.class) // getMapping, Postmapping 등등 비슷한거 // 예외가 발생하면 이쪽으로 와서 오류들을 담아서
    // 400 에러로 에러를 보내주겠다
    public ResponseEntity<?> validException(ValidException e) {
        return ResponseEntity.badRequest().body(e.getFieldErrors());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.badRequest().body(Set.of(new FieldError("authentication", "authentication", e.getMessage())));
    }

    // UsernameNotFoundException과 BadCredentialsException은 같은데 그냥 username 틀렸을 때와 password 틀렸을 때를
    // 구분하기 위해 나눴다. -> 틀리면 로그가 나와서 개발자들이 오류 찾기 쉽다
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsException(BadCredentialsException e) {
        // 같은 형식으로 주면 프론트앤드가 작업하기 편하다. -> 위에 UsernameNotFoundException의 authentication과 동일하게 줌
        return ResponseEntity.badRequest().body(Set.of(new FieldError("authentication", "authentication", e.getMessage())));
    }
}
