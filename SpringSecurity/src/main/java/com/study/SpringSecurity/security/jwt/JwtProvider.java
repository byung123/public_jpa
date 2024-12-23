package com.study.SpringSecurity.security.jwt;

import com.study.SpringSecurity.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Component
public class JwtProvider {

    // 이 키 값으로 암호화함 (yml파일 secret)
    private final Key key;

    // 생성자
    // value 어노테이션에 저걸 쓰면 yml; 파일에 jwt의 secret 값을 가지고옴 그것을 String 문자열 값으로 가지고옴(secret 변수로)
    // 자동으로 autowired가 됨
    // JWT를 사용하기 위한 기본 문법임
    public JwtProvider(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // 암호화(Decoder)해서 넣어줘야함
    }

    // Bearer 문자 잘라주기
    public String removerBearer(String token) {
        // 콘솔창에서 Bearer를 잘라주려면 숫자 7만 입력하기 보단 밑처럼 해주는 것이 좋음
        // 해주는 이유는 Bearer가 아닌 다른 이름으로도 들어올 수도 잇기 때문에 ex) AccessBearer로 날라올 수도 잇을 때
        // 그것을 구분하기 위해 밑처럼 해주는 것이 좋다
        return token.substring("Bearer ".length());
    }

    // 토큰을 생성해주는 메서드
    // 토큰 자체는 문자열이라 String
    // 사용자 정보를 이용해 토큰을 만들어야 해서 User를 받아옴
    public String generateUserToken(User user) {

        // 오늘 날짜와 시간(getTime())을 다시 새로운 Date에 넣으면 다시 Date 형식으로 됨
        // 1000: 1초 -> 총 한달정도 동안 토큰을 유지시켜주기 위해 30까지 곱함
        // int 범위를 넘어가서 L을 붙여서 Long 자료형으로 해줘야함
        Date expireDate = new Date(new Date().getTime() + (1000L * 60 * 60 * 24 * 30));

        // 토큰 만들기 - 밑의 문법 자체로 토큰이 하나 만들어짐
//        String token = Jwts.builder() // 토큰 생성하려면 JWT가 아닌 Jwts로 해야함 -> builder.compact()로 마무리해야함
//                .compact();

        // 토큰에 user정보를 넣기 위해 중간에 user 정보를 넣어줘서 빌드함(유저정보는 서비스에서 받아오나?)
        // claim은 키 밸류 값을 추가할 때 사용함 - 가져오는 정보가 JSON 형태기 때문에
        // signwith(key) : 이 key를 가지고 복호화 해야한다. (암호화 목적)
        // 토큰은 중간중간 만료시켜줘야 하기 때문에 시간을 정해줌 : expiration(time 형식) : 만료시간 정하기(위에서 Date 선언)
        String token = Jwts.builder()
                .claim("userId", user.getId())
                .expiration(expireDate)
//                .signWith(key)
                .signWith(key, SignatureAlgorithm.HS256) // 암호화할 때 HS256 알고리즘을 쓰겟다
                .compact();

        return token;
    }


    public Claims parseToken(String token) {
        // parser : 원래대로 parsing 하겠다
        // setSignKey 는 자동완성 띄우면 줄이 그어져잇는데 이건 곧 없어질거니 웬만하면 다른거 쓰라는 뜻
        JwtParser jwtParser =  Jwts.parser() // Jwts 객체를 변환한다는 뜻 -> PARSER 객체로 생성하겠다.
                .setSigningKey(key) // 이것으로 이제 암호화를 풀수 있다
                .build();

//                jwtParser.parseClaimsJws(token) // 생긴 PARSER객체로 parseClaimsJws를 할 수 잇다 -> 이 토큰을 claims로 변환시켜서
//                .getPayload();    // return type이 P인데, 자동완성보면 claims를 리턴하게 된다 -> claims를 꺼내와라
        return jwtParser.parseClaimsJws(token).getPayload();
    }
}
