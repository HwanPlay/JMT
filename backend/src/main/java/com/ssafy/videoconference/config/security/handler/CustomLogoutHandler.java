package com.ssafy.videoconference.config.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.videoconference.model.user.bean.User;

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
		
		User user = null;
		try {
			user = (User)new ObjectMapper().readValue(request.getInputStream(), User.class);
			System.out.println("logout id : " + user.getId());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String accessToken = user.getId() + "_accessToken";
		String refreshToken = user.getId() + "_refreshToken";
		
		redisTemplate.delete(accessToken);
		redisTemplate.delete(refreshToken);

		response.setStatus(HttpStatus.OK.value());
	}

}
