package com.study.SpringSecurity.aspect;

import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.exception.ValidException;
import com.study.SpringSecurity.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Slf4j
@Aspect
@Component // signupService를 autowired 할 수 있게됨
public class ValidAspect {

    @Autowired
    private SignupService signupService;

    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.ValidAop)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();  // ars안에 dto와 bindingresult가 들어와 있다 (매개변수 들고옴)
        BeanPropertyBindingResult bindingResult = null; // BeanPropertyBindingResult의 객체를 초기화 했음(아까 들고온 매개변수 중 bindingresult 초기화)

        // 매개변수의 순서가 어떻게 되있는지 모르기 때문에 bindingresult를 찾아줘야하기 때문에 반복을 돌려 찾는다
        for(Object arg : args) { // Object 자료형으로 업캐스팅해서 반복을 돌리는데, 이 Object 클래스 안에는 getClass가 있다
//            System.out.println(arg.getClass().getSimpleName()); 뭐가 잇는지 확인
            if(arg.getClass() == BeanPropertyBindingResult.class) {
                bindingResult = (BeanPropertyBindingResult) arg; // Object 자료형을 다운캐스팅 해줘야한다
                break;
            }
        }

        // checkPassword 확인할 때 쓰는 구문
        // 부가적인 로직은 aspect를 뺴놓음
        // proceedingJoinPoint : 핵심 로직, Signature : 클래스명, 패키지경로, 메서드 명, 파라미터이름, 타라미터 타입 등 정보 가져옴
        // getName : 이 동작의 주체는 signup[ 메소드이기 때문에 , 메서드 명을 가져온다
        // 즉, proceedingJoinPoint.getSignature().getName()은 signup이 출력
        switch (proceedingJoinPoint.getSignature().getName()) {
            case "signup":
                validSignupDto(args, bindingResult);
                break;
                    // for 문을 사용하니 코드가 더러워져서 밑에 validCheckPassword 함수에 따로 정의해둠
//                for(Object arg : args) { // 이번엔 받은 매개변수 중 dto를 찾아내서 checkPassword와 password를 비교해줘야한다
//                    if(arg.getClass() == ReqSignupDto.class) { // 해당 dto가 있을 때 실행
//                        ReqSignupDto dto = (ReqSignupDto) arg;
//                        if(!dto.getPassword().equals(dto.getCheckPassword())) { // 만약 에러가 잇으면
//                            FieldError fieldError = new FieldError("checkPassword", "checkPassword", "비밀번호가 일치하지 않습니다.");
//                            bindingResult.addError(fieldError); // 또 fieldError를 bindingResult에 넣어준다
//                        }
//                        break;
//                    }
//                }
        }

//        for(FieldError fieldError : bindingResult.getFieldErrors()) { // 에러가 없으면 실행 x,
//            System.out.println(fieldError.getField()); // 필드네임
//            System.out.println(fieldError.getDefaultMessage()); // 메세지
//        }

        // 유효성 검사 오류 -> 이건 클라이언트가 잘못 입력한 것이기 때문에 400에러를 띄어줘야한다.
        if(bindingResult.hasErrors()) { // 에러가 있으면 요 예외처리를 생성해 실행시켜준다
            // 예외를 생성한 후 ExceptionControllerAdvice으로 throw 해줌
            throw new ValidException("유효성 검사 오류", bindingResult.getFieldErrors()); 
        }

        return proceedingJoinPoint.proceed();
    }

    // 바인딩리절트 객체가 주소기 때문에 리턴이 필요없다?
    private void validSignupDto(Object[] args, BeanPropertyBindingResult bindingResult) {
        for(Object arg : args) { // 이번엔 받은 매개변수 중 dto를 찾아내서 checkPassword와 password를 비교해줘야한다
            if(arg.getClass() == ReqSignupDto.class) { // 해당 dto가 있을 때 실행
                ReqSignupDto dto = (ReqSignupDto) arg;
                if(!dto.getPassword().equals(dto.getCheckPassword())) { // 만약 에러가 잇으면
                    FieldError fieldError = new FieldError("checkPassword", "checkPassword", "비밀번호가 일치하지 않습니다.");
                    bindingResult.addError(fieldError); // 또 fieldError를 bindingResult에 넣어준다
                }
                if(signupService.isDuplicatedUsername(dto.getUsername())) {
                    FieldError fieldError = new FieldError("username", "username", "이미 존재하는 사용자이름입니다.");
                    bindingResult.addError(fieldError);
                }
                break;
            }
        }
    }
}
