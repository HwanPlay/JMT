package com.ssafy.videoconference.config.security.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ssafy.videoconference.model.user.bean.User;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken authRequestToken;
		System.out.println("token : " + request.getMethod());
		// 로그인 request method는 POST로 전달하자
		if (!HttpMethod.POST.name().equals(request.getMethod())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Authentication method not supported. Request method: " + request.getMethod());
			}
			throw new AuthenticationServiceException("Authentication method not supported");
		}
		System.out.println("여기?");
		System.out.println(request.getParameter("id"));
		// login 정보를 한 번에 User클래스에 매핑
//			User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

		User user = new User();
		user.setId(request.getParameter("id"));
		user.setPw(request.getParameter("pw"));
		
		System.out.println(user.toString());
		
		// UsernamePasswordAuthenticationToken으로 반환
		authRequestToken = new UsernamePasswordAuthenticationToken(user.getId(), user.getPw());
		logger.info("User attempt authentication. userId={}", user.getId());

		// Allow sebclasses to set the "details" property
		setDetails(request, authRequestToken);

		return this.getAuthenticationManager().authenticate(authRequestToken);
	}

}
