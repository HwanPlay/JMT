package com.ssafy.videoconference.config.security.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.ssafy.videoconference.config.util.JwtTokenUtil;
import com.ssafy.videoconference.model.user.bean.UserDetail;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
    RedisTemplate<String,Object> redisTemplate;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		UserDetail userDetail = (UserDetail) authentication.getPrincipal();
		System.out.println("success : " + userDetail.toString());
		final String accessToken = jwtTokenUtil.generateAccessToken(userDetail);
		final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetail.getUser().getId());
		
		System.out.println("Success Handler");
		response.addHeader("AccessToken", "Bearer " + accessToken);
		response.addHeader("RefreshToken", "Bearer " + refreshToken);
	
		
		ValueOperations<String,Object> redis = redisTemplate.opsForValue();
	
		redis.getAndSet(userDetail.getId()+"_accessToken", accessToken);
//		redisTemplate.opsForValue().set(accessToken, true);
//		redisTemplate.expire(accessToken, 10*6*1000, TimeUnit.MILLISECONDS);
		
		redis.getAndSet(userDetail.getId()+"_refreshToken", refreshToken);
		
		response.setStatus(HttpStatus.OK.value());
		
	}

}