package com.study.SpringSecurity.security.principal;

import com.study.SpringSecurity.domain.entity.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// principal 안에는 username, password, getAuthorities와 같은 권한들이 들어 있다.
@Builder
@Data
public class PrincipalUser implements UserDetails {
    // ctrl + i

    private Long userId;
    private String username;
    private String password;
    private Set<Role> roles;

    // 권한
    // Collection return : List나 Set return 해줘야함. GrantedAuthority이 개체를 상속받은 녀석만 할 수 있다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        new SimpleGrantedAuthority();
//        return List.of();

        //밑에 stream 코드를 안쓰면 이런식으로 할 수도 있음
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        for(Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return authorities;

        // GrantedAuthority의 자료형으로 바꿔줘야함 하나하나
        // 스트림으로 바꾼후 role 하나하나 꺼내서 바꾸고 다시 set으로 바꿔주겠다는 뜻
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    // Data 어노테이션 덕분에 getter setter 어차피 할 수 있음
//    @Override
//    public String getPassword() {
//        return "";
//    }
//
//    @Override
//    public String getUsername() {
//        return "";
//    }

    // 계정이 만료되었다, 임시계정, 체험 계정 같은거 할 때 자주 사용
    @Override
    public boolean isAccountNonExpired() {
        return true; // 밑에꺼까지 다 true로 바꾸자. 하나라도 안되면 예외뜸
    }

    // 계정이 잠긴 것
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 인증이 만료된 것 -> 보통 2가지 경우 나눠짐 : 비밀번호가 5번 이상틀릴 경우, 1년동안 비번 안바꿔서 만료됐을 때 등
    // 이걸로 비밀번호 변경 페이지로 보내줌
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //활성화 됐는지 안됐는지
    @Override
    public boolean isEnabled() {
        return true;
    }
}
