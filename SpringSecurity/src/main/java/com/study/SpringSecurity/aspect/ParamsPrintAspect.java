package com.study.SpringSecurity.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ParamsPrintAspect {

    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.ParamsAop)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 매개변수가 잘 들어왔는지는 전처리에서 확인함 메소드 실행 후 확인하는건 무의미

        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = proceedingJoinPoint.getArgs();

        String infoPrint = "ClassName(" + signature.getDeclaringType().getSimpleName() + ") MethodeName("
                + signature.getName() + ")";
        String linePrint = "";
        for(int i = 0; i < infoPrint.length(); i++) {
            linePrint += "-";
        }

        log.info("{}", linePrint);
        log.info("{}", infoPrint);
//        log.info("======{}.{}======",
//                signature.getDeclaringType().getSimpleName(), // signature 안의 클래스(타입)의 이름만 가지고 와라
//                signature.getName()); // 메소드명을 가지고 와라
        for(int i = 0; i < paramNames.length; i++) {
            log.info("{} >>>> {}", paramNames[i], args[i]);
        }
        log.info("{}", linePrint);

        return proceedingJoinPoint.proceed(); // 전처리만 있을거라 바로 리턴에 넣어줌 (후처리 없음)
    }


}
