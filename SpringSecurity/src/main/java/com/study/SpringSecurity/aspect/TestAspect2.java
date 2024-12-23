package com.study.SpringSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 기본적인 AOP 사용 문법 (AOP를 만들어라)
@Aspect
@Component
@Order(value = 1) // testaop 함수 실행 순서 바꾸고 싶을 때, 1로하고 testAspect testAop()를 1로 하면 test2가 먼저 실행됨
public class TestAspect2 {

    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.Test2Aop)") // 어노테이션 클래스가 있는 곳 경로 복사 붙여넣기
    private void pointCut() {}
//    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.Test2Aop)") // 어노테이션 클래스가 있는 곳 경로 복사 붙여넣기
//    private void pointCut2() {}
//    @Around("pointCut()&pointCut2") // 이렇게 이을 수도 있지만 보통 이렇게 연결해서 쓰진 않고 어노테이션을 이용함

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        for(Object obj : proceedingJoinPoint.getArgs()) { // 매개변수의 값들을 하나씩 꺼내올 수 있다
//            System.out.println(obj);
//        }

//        Signature signature = proceedingJoinPoint.getSignature(); 원래 코드시그니처임
        // 훨씬 더 많은 메소드를 받아올 수 있음, 예를 들어 매개변수 타입 등(getParameterType)
        // 이것을 이용해서 이런 매개변수를 가지고 있는 녀석을 실행하라는 조건을 명시해줄 수 있게됨
        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();
        System.out.println(signature.getName()); // 메소드 명이 출력됨
        System.out.println(signature.getDeclaringTypeName()); // 그 메소드가 있는 클래스명을 출력

        String[] paramsNames = signature.getParameterNames();
        Object[] args = proceedingJoinPoint.getArgs();

        for(int i = 0; i < args.length; i++) { // 매개변수의 값들을 하나씩 꺼내올 수 있다
            System.out.println(paramsNames[i] + " : " + args[i]);
        }

        System.out.println("전처리2");
        Object result = proceedingJoinPoint.proceed();
        System.out.println("후처리2");

        return result;
    }
}
