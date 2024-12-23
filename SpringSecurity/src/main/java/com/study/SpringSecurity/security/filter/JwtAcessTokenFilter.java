package com.study.SpringSecurity.security.filter;

import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.repository.UserRepository;
import com.study.SpringSecurity.security.jwt.JwtProvider;
import com.study.SpringSecurity.security.principal.PrincipalUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAcessTokenFilter extends GenericFilter {

    @Autowired
    private JwtProvider jwtProvider;

    // userId를 가지고 user 불러오기위한것
    @Autowired
    private UserRepository userRepository;

    // 추상메소드 오버라이드 (ctrl + i)
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 위의 매개변수를 사용하려면 다운캐스팅 해줘야함
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 수동으로 앞에 bearer를 떼고만들어줘야함 -> 공통으로 하는 작업이기 때문에 JwtProvider에 해당 과정을 넣어줌

        // accessToken을 가져올건데 header에서 Authorizationd의 키값으로 들고온다
        String bearerAccessToken = request.getHeader("Authorization");
        // 토큰이 null일 수도 있음
        if (bearerAccessToken != null) {
            String accessToken = jwtProvider.removerBearer(bearerAccessToken);
//            jwtProvider.validToken(accessToken);
            Claims claims = null;
            try {
                 claims = jwtProvider.parseToken(accessToken);
            } catch (Exception e) {
                // 이걸 기준으로 전처리 후처리 // 이걸 해줘야ㅕ 다음 필터나 요청으로 넘어감
                filterChain.doFilter(servletRequest, servletResponse);
                return; // 이거 안해주면 다음 필터로 넘어가서 해줘야함
            }
            // claims.get이 object라 다운캐스팅 필요 -> interger 바꿨다가 다시 Long으로
            Long userId = ((Integer) claims.get("userId")).longValue();
            Optional<User> optionalUser = userRepository.findById(userId);
            if(optionalUser.isEmpty()) { // 토큰은 존재하는데 DB에 없는 경우 -> 아이디가 삭제됐는 경우
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            // optionalUser가 있으면 가져오기
            PrincipalUser principalUser = optionalUser.get().toPrincipalUser();
            // setAuthen어쩌고를 true로 해줘야 하기 때문에 매개변수 3개 짜리를 사용해야한다
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(), principalUser.getAuthorities());

            System.out.println("예외 발생하지 않음");
            // 중요!!
            // SecurityContextHolder이 안에 setAuthentication가 있어야 인증이 되는 것이고
            // Authentication이 있는지 확인 -> 이걸 하고 필터로 넘김?
            // 그럼 SecurityConfig에 before로 설정한 UsernamePasswordAuthenticationFilter서 자동으로 비교해줌??
            // 무조건 setAuthentication을 넣어줘야지만 인증이 된거다
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        System.out.println(bearerAccessToken);
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
