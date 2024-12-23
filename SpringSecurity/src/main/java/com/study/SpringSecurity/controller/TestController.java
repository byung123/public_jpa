package com.study.SpringSecurity.controller;

import com.study.SpringSecurity.security.principal.PrincipalUser;
import com.study.SpringSecurity.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 안에 ResponseBody 들어 있는데
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    // 401에러나옴(인증되지 않은 요청자) - dependency에 security 가 있어서
    @GetMapping("/test")
    public ResponseEntity<?> get() {
//        System.out.println("get메소드 호출"); // testService.aopTest() 가 호출되기 전 관점 -> 나중에 부가기능으로 만들거임

        // 핵심기능이 됨 -> (RestAspect 부분에서) -> 여기가 동작하면 aspect의 @around 어노테이션이 있는 부분이 먼저 호출이 된다.
        // 그러면 바로 appTest()가 먼저 실행되는 것이 아닌 aspect 안의 전처리가 먼저 동작하고, aopTest() 호출 결과(proceed 부분)
        // 저장된 후 후처리가 일어나고 그 다음 result 값을 들고와서 sout이 일어난다
        // 즉, aspect 후처리가 일어난 다음 return 값을 받고 이것이 실행된다.
        // 콘솔창 순서 : 전처리 -> "Aop 테스트 입니다." -> 후처리 -> "AOP 테스트 입니다." 순서로 나옴
//        System.out.println(testService.aopTest());
//        testService.aopTest2("김준일", 31);
//        testService.aopTest3("010-1111-2222", "부산 동래구");

//        System.out.println("get메소드 리턴되기 직전"); // testService.aopTest() 가 호출되기 후 관점 -> 나중에 부가기능이 됨

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // principalUser가 Object로 들어가있음( 아까 들어갈때 Object로 업캐스팅해서 드갔기 때문 매개변수로) 그래서 다시 다운캐스팅 필요
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();

//        return ResponseEntity.ok("확인");
        return ResponseEntity.ok(principalUser);
    }


}
