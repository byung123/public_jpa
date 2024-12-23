package com.study.SpringSecurity.config;

import com.study.SpringSecurity.security.filter.JwtAcessTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 아이콘 모양 추상클래스를 상속시킴(WebSecurityConfigurerAdapter)
// 빨간줄이 안뜨는 걸 보면 아직 구현안된 추상메소드가 없다는 뜻(abstract)
// 안에 이름만 정의된 메소드들도 있다 => 오버라이드해서 써도 되고 안써도 된다는 뜻
// 추상메소드가 있다면 무조건 재정의 해서 사용해야 한다
// 참고로 오버라이드는 매개변수명 이름은 바꿀 수 있다.

// IOC 컨테이너에 빈 객체로 등록하기 위해 Configuration을 붙임
@EnableWebSecurity // 우리가 만든 SecurityConfig를 적용시키겠다는 뜻 -> 상속받은 메소드 자체를 사용 하지 않고 다른걸 쓰,겠다?
@Configuration // 안에다가 bean 등록을 해주는 어노테이션 -> @bean 어노테이션을 사용할 수 있게된다
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 필터도 컴포넌트로 등록해야 필터 사용가능
    @Autowired
    private JwtAcessTokenFilter jwtAcessTokenFilter;

    // 이녀석은 원래 component 어노테이션을 못달아서 원래 ioc에 component로 등록하지 못한다 -> 그래서 Configuration 달려
    // 있는 클래스에 @Bean 어노테이션을 여기서 생성된 new BCryptPasswordEncoder 하나가 ioc에 등록될 수 있게 된다
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ctrl + o : 오버라이드
    // ctrl + i 는 추상메소드가 없어서 안뜸
    // 자물쇠 잠긴 건 메소드가 private이냐 아니냐 차이
    // 노란색깔 자물쇠는 protected
    // configure가 2개 있음(web, http)
    // web: web이 매개변수명, 뒤에가 매개변수 타입
    // http: 동일한 메소드인데 매개변수가 다름(오버로딩)
    // 우리는 http 프로토콜로 통신하고 있기 때문에 http를 오버라이드 한다.
    // default: 동일한 패키지 내에서만 접근 가능, 참조 가능
    // protected: 동일한 패키지 안에서 참조가능한데, 상속 관계가 있으면 다른 패키지에서도 참조 가능한 것
    // 즉 밑의 메소드도 같은 패키지는 아니지만 상속받았기 때문에 protected 메소드는 사용할 수 있는것
    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        super.configure(http); // 어차피 사용 안핡더기 때문에 버임
        // configure 안에
        // http.formlogin() 쓸거야, httpBasic 쓸거야 하고 있는것 -> 아까 기본 페이지가 이것임
        http.formLogin().disable(); // 기본 로그인 페이지 안씀 -> 서버 재실행하고 아까 url 드가면 다른게 뜸 그런데 이것도 안쓸거라
        http.httpBasic().disable(); // 이제 그 폼창도 안뜨고 엑세스 자체가 거부가 됨 (403에러 : 요청 자체를 막음, 로그인 기회 자체를 안줌)
//        http.authorizeRequests((requests) -> { 이건 모든 요청에 대한 인증을 하라는 메소드임
//            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)requests.anyRequest()).authenticated();
//        });
//        http.sessionManagement().disable(); // 여기서 제공하는 세션도 안쓸거다(세션 안쓰고 우린 JWT를 쓸거라서)
        // 스프링 시큐리티가 세션을 생성하지도 않고 기존의 세션을 사용하지도 않겠다

        // 스프링 시큐리티가 세션을 생성하지 않겠다. 기존의 세션을 완전히 사용하지 않겠다는 뜻은 아님
        // 시큐리티 안에서만 사용하지 않겠다는 뜻임
        // JWT 등의 토큰 인증방식을 사용할 때 설정하는 것 (어차피 토큰 사용할거라 세션 안사용하니까)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors(); // crossOrigin은 허용하는 것, 서버가 서로 다를 때 특정 origin또는 특정 method에 대해서만 접근 가능하게 하겠다.
                     // 서버에 요청은 할 수 있지만 허가 된거 외에는 응답을 안해준다
        http.csrf().disable(); // 위조방지 스티커(토큰)
        // 서버사이드 랜더링에서 html을 만들어서 클라이언트에서 전달할 때 csrf ㅌ호큰을 만들어서 준다
        // 나중에 다시 클라이언트가 정보를 보낼 때 이 토큰(csrf)이 없으면 서버 쪽에서 위조된 것이라 판단할 수 있음
        // 하지만 우리는 다른 토큰을 사용할 것이기 때문에 이것을 사용하지 않음 (어차피 세션 사용 안해서 필요없음)
        // 다음에 만들땐 disable할 것들 모아서 코드 붙여놓자

        // 여기까지가 JWT를 사용하기 위한 기본 세팅


        http.authorizeRequests()
//                .antMatchers("/auth/**", "/test/**") // 주소 몇개를 골라주는 메서드( auth/로 시작되는 모든 주소들을 뜻함)
//                .antMatchers("/signin", "/signup")
                .antMatchers("/auth/**", "/h2-console/**") // Controller에서 RequesMapping으로 설정해놔서 그냥 이렇게 사용
                .permitAll() // 위의 주소로 입력된 모든
                // 것들은 접근 허용해주겠다.(인증없이), 실제 /test로 입력하면 그냥 넘어감
                             // auth도 마찬가지지만 auth 경로에 해당하는 controller가 없어서 이건 404가 뜸 -> 어쨋든 요청에 대한 응답은 옴
                             // 나머진 요청 요구 자체가 안뜨는 에러 페이지가 나옴
                .anyRequest()
                .authenticated() // 다른 모든 요청들은 인가를 거쳐라는 뜻, (로그인 키 값을 달라는 요청마저도 인증을 해야 가능하다)
                .and()
                .headers()
                .frameOptions() // h2-console 때매 썼다는데 안써도되고 써도된다 하심
                .disable();

        // 이 필터가 실행되기 이전에 오른쪽 필터 먼저 추가하라는 뜻 (반대인듯)
        http.addFilterBefore(jwtAcessTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }


}
