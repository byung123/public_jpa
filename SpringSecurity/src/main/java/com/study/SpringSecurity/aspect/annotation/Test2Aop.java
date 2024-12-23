package com.study.SpringSecurity.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Test2Aop를 실행할 때 이 어노테이션을 실행시키겠다
@Target({ElementType.METHOD}) // 메소드 위에 어노테이션을 달 수 있다는 뜻, field면 변수위에 어노테이션 달 수 있다
public @interface Test2Aop {}
