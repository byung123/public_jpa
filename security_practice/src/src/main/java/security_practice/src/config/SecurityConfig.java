package security_practice.src.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security_practice.src.security.filter.JwtAccessTokenFilter;

@EnableWebSecurity  // 시큐리티 라이브러리 사용가능
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAccessTokenFilter jwtAccessTokenFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable(); // formLogin 사용x
        http.httpBasic().disable(); // forLogin http 창 사용 x
        http.sessionManagement().disable(); // 세션 사용 x
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);    // 세션을 완전 안쓰지는 않겠다?
        http.cors();    // 크로스 오리진 사용
        http.csrf().disable();   // 위조방지 스티커
        http.authorizeRequests()
                .antMatchers("/auth/**", "/h2-console/**")  // 여기 주소 허용
                .permitAll()    // 위 주소로 입력된 것은 모두 접근 허용
                .anyRequest()   // 위 주소를 제외한 다른 주소
                .authenticated()    // 위 주소를 제외한 주소들은 인증을 받아야 한다.
                .and()
                .headers()
                .frameOptions()
                .disable();
        http.addFilterBefore(jwtAccessTokenFilter, UsernamePasswordAuthenticationFilter.class); // 저 긴 필터 지나기 전에 만든 필터 먼저
    }
}
