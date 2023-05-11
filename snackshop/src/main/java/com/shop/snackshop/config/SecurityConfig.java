package com.shop.snackshop.config;



import com.shop.snackshop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//@Configuration 어노테이션을 사용하면 가시적으로 설정파일이다 ~ , Bean 등록할꺼다 ~ 라는걸 알수있다.(=@Bean을 수동으로 등록할 수 있다)
//@EnableWebSecurity = 시큐리티 활성화 시키기 위한 어노테이션
//WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity 어노테이션을 선언하면,
//SpringSecurityFilterChain이 자동으로 포함된다.
//WebSecurityConfigurerAdapter를 상속받아서 메소드 오버라이딩을 통해 보안 설정을 커스터마이징 할 수 있다.

//WebSecurityConfigurerAdapter=스프링 시큐리티의 웹 보안 기능 초기화 및 설정 ㅡㅡㅡ→ HttpSecurity=세부적인 보안 기능을 설정할 수 있는 api제공
//          ↑
//          | 상속
//          |
//     SecurityConfig=사용자 정의 보안 설정 클래스
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        http.authorizeRequests()
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**")
                .permitAll()
                .mvcMatchers("/admin/**")
                .hasRole("ADMIN")
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**","/assets/**", "/favicon.ico", "/resources/**","/error");
    }
    //@Bean을 사용하는 이유
    //1.개발자가 직접 제어가 불가능한 라이브러리를 활용할 때
    //2.애플리케이션 전범위적으로 사용되는 클래스를 등록할 때
    //3.다형성을 활용하여 여러 구현체를 등록해주어야 할 때
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}
