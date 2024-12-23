package com.study.SpringSecurity.service;

import com.study.SpringSecurity.aspect.annotation.TimeAop;
import com.study.SpringSecurity.domain.entity.Role;
import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.repository.RoleRepository;
import com.study.SpringSecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SignupService {

    // Autowired가 많아져서 코드를 보기 쉽게 한 줄로 정리
//    @Autowired private UserRepository userRepository;
//    @Autowired private RoleRepository roleRepository;
//    @Autowired private UserRoleRepository userRoleRepository;
//    @Autowired private BCryptPasswordEncoder passwordEncoder;

    // 강사님은 위에 보다 이렇게 쓰는걸 선호하신다 하심
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
//    private final UserRoleRepository userRoleRepository; // UserRole 테이블 지워서 사용안함
    private final BCryptPasswordEncoder passwordEncoder;

    @TimeAop // 데이터베이스에 등록되는데 걸리는 시간(초)를 볼 수 있음
    // User 테이블은 들어갔는데UserRole은 들어가지 않은채 저장되면 오류뜸? 그래서 하는거?
    // 이 메소드 실행하다가 예외가 터지면 rollback하라는 뜻
    // Exception.class 하면 이 안에있는 모든 예외 클래스를 대상으로 실행된다.
    // 이메소드가 정상 종료 되면 예외없이 그냥 commit이 됨
    // 참고로 Mybatis에서도 똑같이 적용할 수 있음
    @Transactional(rollbackFor = Exception.class)
    public User signup(ReqSignupDto dto) {
//        userRepository.findByUsername(dto.getUsername());
        User user = dto.toEntity(passwordEncoder);
        Role role = roleRepository.findByName("ROLE_USER").orElseGet( // true가 아니면(ROLE_USER가 없으면) get해라
                () -> roleRepository.save(Role.builder().name("ROLE_USER").build())
                // ROLE_USER가 save되고, 다시 그걸 가져와서 return 해줌. 그걸 role 변수에 넣어줌
        );
        user.setRoles(Set.of(role)); // 이게 있어야 save할 때 같이 save 된다
        user = userRepository.save(user); // UserRole 테이블을 수동으로 만들어서 순서가 달라짐 - user 엔티티에 덮어씀
        // 그러면 이제 UserRole을 생성할 수 있게 된다
//        UserRole userRole = UserRole.builder()
//                .user(user)
//                .role(role)
//                .build();
//        // UserRole repository 필요, 위에 autowired 생성
//        userRole = userRoleRepository.save(userRole);
//        user.setUserRoles(Set.of(userRole)); // 이걸 왜 지우시지

        return user; // user가 UserRoles를 조인한 테이블을 리턴 해야하는데 위에꺼 지워서 나올수가 있니?

//        user.setRoles(Set.of(role)); // user가 role도 가진채로 저장된다
//        return userRepository.save(user); // -> join Table 됨 -> User Table
        // h2 url에 테이블 3개된거 확인, 아까 회원가입한 아이디와 비밀번호로 로그인
        // 했을 때 콘솔창으로 로그인 성공 메세지 오는지 확인
    }

    public boolean isDuplicatedUsername(String username) {
        return userRepository.findByUsername(username).isPresent(); // isPresent() : user가 null 인지 체크를 해줌
    }
}
