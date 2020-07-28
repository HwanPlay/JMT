package com.ssafy.videoconference.config.securiy;

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

import com.ssafy.videoconference.config.security.filter.CustomAuthenticationFilter;
import com.ssafy.videoconference.config.security.handler.CustomLoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String[] PUBLIC = new String[] { "/error", "/login", "/logout", "/register", "/mypage",
			"/api/registrations" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// rest api는 token authentication. csrf 보안 필요 X
				.csrf().ignoringAntMatchers("/api/*")

				.and()
				// 다음 request에 대한 사용권한 check
				.authorizeRequests().antMatchers(PUBLIC).permitAll().anyRequest().authenticated()

				.and()
				// 세션 사용 X
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

//				.and()
//				// jwt token 필터를 id/password 인증 필터 전에 추가
//				.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//
				.and()
				// form login 사용 X. JSON 형식으로 사용자 정보 요청
				.formLogin().disable();

	}

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(passwordEncoder());
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider());
    }
    
    
	@Override // ignore check swagger resource
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
				"/swagger/**");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
