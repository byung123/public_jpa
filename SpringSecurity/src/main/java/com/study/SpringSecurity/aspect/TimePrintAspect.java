package com.study.SpringSecurity.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class TimePrintAspect {

    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.TimeAop)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();
        StopWatch stopWatch = new StopWatch(); // 스프링부트에서 자체적으로 제공해주는 객체

        stopWatch.start(); // 스탑워치 시작
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop(); // 스탑워치 종료

        String infoPrint = "ClassName(" + signature.getDeclaringType().getSimpleName() + ") MethodeName("
                + signature.getName() + ")";
        String linePrint = "";
        for(int i = 0; i < infoPrint.length(); i++) {
            linePrint += "-";
        }

        log.info("{}", linePrint);
        log.info("{}", infoPrint);
        log.info("TotalTime: {}초", stopWatch.getTotalTimeSeconds()); // getTotalTimeSeconds : 초단위로 가져온다
        log.info("{}", linePrint);

        return result;
    }


}
