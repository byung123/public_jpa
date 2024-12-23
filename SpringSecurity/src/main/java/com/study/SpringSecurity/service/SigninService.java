package com.study.SpringSecurity.service;

import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.dto.request.ReqSigninDto;
import com.study.SpringSecurity.dto.response.RespJwtDto;
import com.study.SpringSecurity.repository.UserRepository;
import com.study.SpringSecurity.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SigninService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider; // 유저 정보를 토큰에 넘겨주기 위해 생성

    public RespJwtDto signin(ReqSigninDto dto) {
        // username 확인
        // optional로 자료형 가져와서 .orElseThrow를 함
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("사용자 정보를 다시 입력하세요.")
        );
        // password 확인
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) { // 다르면
            throw new BadCredentialsException("사용자 정보를 다시 입력하세요.");
        }

        System.out.println(user);

        System.out.println("로그인 성공");

        // 토큰에 유저 정보 넣어주고 토큰 리턴해줌
        // 근데 토큰만 문자열로 리턴해주기 보다 dto를 만들어서 보내주기 위해 RespJwtDto를 만듬
        // RespJwtDto를 최종적으로 반환 (위에 메소드 자료형도 최종적으로 dto로 바꿔줌)
        return RespJwtDto.builder().accessToken(jwtProvider.generateUserToken(user)).build();
    }
}
