package com.study.SpringSecurity.dto.request;

import com.study.SpringSecurity.domain.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
public class ReqSignupDto {

    // 이런식으로 사용하지 못한다. 왜? BCryptPasswordEncoder는 여기서 ReqSignupDto가 IOC로 등록되지 않았기 때문에
    // autowired 할수 없다, SecurityConfig여기서 Bean으로 등록했는데도 안되는건가?
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;

    // 정규식을 설정함(데이터 규칙), 메세지 설정, 정규식은 메세지 내용 복사해서 gpt한테 물어보면 친절히 알려줌, 또는 구글에 정규표현식 검색해서 알아보기
    @Pattern(regexp = "^[a-z0-9]{6,}$", message = "사용자이름은 6자이상의 영소문자, 숫자 조합이어야합니다.")
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*?])[A-Za-z\\d~!@#$%^&*?]{8,16}$", message = "비밀번호는 8자이상의 16자 이하의 영대소문, 숫자, 특수문자(~!@#$%^&*?)를 포함해야합니다.")
    private String password;
    private String checkPassword;
    @Pattern(regexp = "^[가-힣]+$", message ="이름은 한글이어야합니다.")
    private String name;

    // 원래는 username 중복인지, 비밀번호 비었는지 몇자 이상인지 등등 검증하는거 만들어줘야하는데 지금은 일단
    // 기본 학습할거라 만들지 않음
    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .build();
    }
}
