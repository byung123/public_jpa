package com.study.SpringSecurity.init;

import com.study.SpringSecurity.domain.entity.Role;
import com.study.SpringSecurity.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataLoader implements CommandLineRunner { // 프로그램이 실행되면(RoleDataLoader) 바로 자동으로 실행되는 클래스()
// 실제로 서버 실행을 하면 밑에 바로 insert 구문이 나오는 것을 확인할 수 잇음 -> 그리고 h2- url에 DB에 접속해서 있는 지 확인
    // select * from ROLE;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception { // ...은 매개변수로 문자열타입 매개변수를 여러개를 넣을 수 있다는 뜻
        // 대신 들어온 args는 배열상태이다.

        // role 테이블에 ROLE_USER라는 이름이 없으면 true
        if(roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_USER").build()); // 없으면 ROLE_USER 라는 컬럼? 추가
        }
        if(roleRepository.findByName("ROLE_MANAGER").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_MANAGER").build()); // 없으면 ROLE_USER 라는 컬럼? 추가
        }
        if(roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_ADMIN").build()); // 없으면 ROLE_USER 라는 컬럼? 추가
        }
    }
}
