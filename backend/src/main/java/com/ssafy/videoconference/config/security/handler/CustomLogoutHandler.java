package com.ssafy.videoconference.config.security.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.ssafy.videoconference.model.user.bean.UserDetail;

public class CustomLogoutHandler implements LogoutHandler {

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		System.out.println("logout - redis delete");
		//request.getParameter("");
//		System.out.println(SecurityContextHolder.getContext());
//		UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		System.out.println(userDetail.toString());
		
		System.out.println("logout id : " + request.getParameter("id"));
		
		String accessToken = request.getParameter("id") + "_accessToken";
		String refreshToken = request.getParameter("id") + "_refreshToken";
		
//		String accessToken = request.getHeader("userId") + "_accessToken";
//		String refreshToken = request.getHeader("userId") + "_refreshToken";
		
		
		redisTemplate.delete(accessToken);
		redisTemplate.delete(refreshToken);

		response.setStatus(HttpStatus.OK.value());
	}

}
