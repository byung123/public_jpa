package security_practice.src.security.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtAccessTokenFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String bearerAccessToken = request.getHeader("Authorization");
        // 서버에서 암호화시킨 토큰인지 검사필요
        // 토큰의 유효기간 검사하기 위한 작업
        if(bearerAccessToken != null) {

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
