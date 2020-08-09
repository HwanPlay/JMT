package com.ssafy.videoconference.config.security;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.ssafy.videoconference.config.security.exception.JwtAuthEntryPoint;
import com.ssafy.videoconference.config.security.filter.CustomAuthenticationFilter;
import com.ssafy.videoconference.config.security.filter.JwtAuthorizationFilter;
import com.ssafy.videoconference.config.security.handler.CustomLoginFailHandler;
import com.ssafy.videoconference.config.security.handler.CustomLoginSuccessHandler;
import com.ssafy.videoconference.config.security.handler.CustomLogoutHandler;
import com.ssafy.videoconference.config.util.JwtTokenUtil;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] PUBLIC = new String[] { "/api/login", "/api/logout", "/api/register/**", "/api/jwt/refresh", "/swagger-ui.html"  };
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// 기본 로그인 페이지 사용 안함
			//	.httpBasic().disable()
				
				// rest api는 token authentication. csrf 보안 필요 X
				.csrf().ignoringAntMatchers("/api/***", "/api/**", "/api/*")
				
				.and()
				.cors()
				
				.and()
				// 다음 request에 대한 사용권한 check
				.authorizeRequests().antMatchers(PUBLIC).permitAll()
				
				.and()
					.authorizeRequests()	
	                .antMatchers("/api/*").hasAnyRole("USER", "ADMIN")
	            .and()
	            	.authorizeRequests()
	            	.anyRequest()
	            	.authenticated()
	                
				.and()
				// 세션 사용 X
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and()
				.logout()
				.logoutUrl("/api/logout")
				.logoutSuccessUrl("/")
				.addLogoutHandler(logoutHandler())
				.invalidateHttpSession(false)
				
				.and()	
				// form login 사용 X. JSON 형식으로 사용자 정보 요청
				.formLogin().disable()
				
				// jwt token 필터를 id/password 인증 필터 전에 추가
				.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtAuthorizationFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
				
				.exceptionHandling().authenticationEntryPoint(customJwtAuthEntryPoint());

				

	}

    // AuthenticationFilter가 로그인 정보를 이용해 UsernamePasswordAuthenticationToken 생성
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtTokenUtil jwtTokenUtil) {
    	return new JwtAuthorizationFilter(jwtTokenUtil);
    }
    
    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }
    
    @Bean
    public CustomLoginFailHandler customLoginFailHandler() {
        return new CustomLoginFailHandler();
    }
    
    @Bean
    public LogoutHandler logoutHandler() {
    	return new CustomLogoutHandler();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(passwordEncoder());
    }

    @Bean
    public JwtAuthEntryPoint customJwtAuthEntryPoint() {
        return new JwtAuthEntryPoint();
    }
    
    // AuthenticationProviders를 추가 및 인증 메커니즘을 설정하는 데 사용
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
    	authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider());
    }
    
    
	@Override // ignore check swagger resource
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
				"/swagger/**", "/jwt/refresh");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
