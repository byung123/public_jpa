package com.study.SpringSecurity.controller;

import com.study.SpringSecurity.aspect.annotation.ParamsAop;
import com.study.SpringSecurity.aspect.annotation.ValidAop;
import com.study.SpringSecurity.dto.request.ReqSigninDto;
import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.service.SigninService;
import com.study.SpringSecurity.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private SignupService signupService;

    @Autowired
    private SigninService signinService;

    // 회원가입 요청 - post
    @ValidAop  // Aop가 2개 달려 있어서 order 순서에 따라 적용순서가 달라진다?
    @ParamsAop //
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody ReqSignupDto dto, BeanPropertyBindingResult bindingResult) {
        // @Valid 어노테이션을 명시해주면 자동으로 유효성 검사를 해주고 매개변수를 전달 시캬준다 (reqSignupDto의 유효성 검사 자동으로 해줌)
        // 만약 규칙에 맞지 않으면 fieldError 생성을 해준다 -> bindinResult에 넣는다 -> 근데 이것은 인터페이스 이므로 ValidAspect에
        // BeanPropertyBindingResult의 객체를 생성시켜서 이 곳에 넣어준다
        // 일치하면 BindinResult를 생성하긴 하지만 아무 값도 안넣은 상태(null)로 생성한다

        // 중복이면 true, 아니면 false를 가지고옴
        // 근데 ValidAspect에서 validSignupDto를 추가해놨기 때문에 굳이 여기서 사용하지 않음
//        boolean isDuplicate = signupService.isDuplicatedUsername(dto.getUsername());


//        System.out.println(dto); log.info() 해도됨
//        if(bindingResult.hasErrors()) { // bindingResult에 에러가 있으면
//            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors()); // 400에러
//        }
        // 위의 모든 과정에서 에러가 발생하지 않으면 이제 signup 메서드를 호출해서 유저 등록을 시켜준다.
        return ResponseEntity.created(null).body(signupService.signup(dto));
    }

    @ValidAop
    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @Valid @RequestBody
            ReqSigninDto dto, BeanPropertyBindingResult bindingResult) {
        signinService.signin(dto);
        return ResponseEntity.ok().body(signinService.signin(dto)); // 토큰 정보 클라이언트에 리턴해주기
    }
}
