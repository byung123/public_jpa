package com.study.SpringSecurity.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE ,ElementType.METHOD}) // TYPE : 클래스 위에 어노테이션 쓸 수 있다는 뜻
public @interface ParamsAop {}
