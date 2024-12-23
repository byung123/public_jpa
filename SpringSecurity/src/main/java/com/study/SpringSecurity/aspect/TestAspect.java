package com.study.SpringSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 기본적인 AOP 사용 문법 (AOP를 만들어라)
@Aspect
@Component // 무조건 IOC에 등록되어 있어야 anotation 어노테이션이라던지 쓸 수 있다?
@Order(value = 2) // 이녀석을 2로 했기 때문에 뒤늦게 실행됨
public class TestAspect {

    // 어떠한 핵심 기능 종이에 한 부분을 자를거고, 그 사이에 핵심 기능을 넣을거다 그 지점이 pointcut 기능 지점(Object result = proceedingJoinPoint.proceed())
    // 그 위치를 잘라서 TestService의 aop로 시작하는 모든 메서드들이 호출되었을 때 아래 @Around 어노테이션 부분이 먼저 시작된다는 의미
    // 이 녀석 경로가 있는 곳을 잘라서 around에 적용시키겠다
    @Pointcut("execution(* com.study.SpringSecurity.service.TestService.aop*(..))")
    private void pointCut() {}

    // 컨트롤러에서 서비스의 aopTest를 호출하면 여기가 먼저 동작 - 필터와 비슷한 동작
    @Around("pointCut()") // 위의 함수명의 호출형태로 들어가줘야한다,
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // proceedingJoinPoint : 핵심기능을 들고 있는 녀석이다

        // 먼저 이것이 실행되고
        System.out.println("전처리"); // 부가기능

        // Service의 aopTest() 메서드의 실행 결과가 result에 들어간다
        Object result = proceedingJoinPoint.proceed(); // 이 안에서 예외가 일어날 수 있기 때문에 예외처리 하라고 빨간줄이 뜬다, // 핵심기능 호출

        // result값이 들어간 후 이것이 실행되고
        System.out.println("후처리"); // 부가기능

        // 후처리까지 끝난 다음 return을 반환
        return result;
    }
}
