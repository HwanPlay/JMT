package com.ssafy.videoconference.config.security.handler;

import java.util.concurrent.TimeUnit;

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
		final String accessToken = jwtTokenUtil.generateAccessToken(userDetail);
		final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetail.getUser().getId());
		
		response.addHeader("AccessToken", "Bearer " + accessToken);
		response.addHeader("RefreshToken", "Bearer " + refreshToken);

		String accessTokenKey = userDetail.getId()+"_accessToken";
		String refreshTokenKey = userDetail.getId()+"_refreshToken";
		
		// 만일, redis에 사용자 jwt token이 남아있다면, delete
		initJwtToken(accessTokenKey,refreshTokenKey);
		
		redisTemplate.opsForValue().set(accessTokenKey, accessToken);
		redisTemplate.expire(accessTokenKey, System.currentTimeMillis() + jwtTokenUtil.JWT_ACCESS_TOKEN_VALIDITY, TimeUnit.MILLISECONDS);
		
		redisTemplate.opsForValue().set(refreshTokenKey, refreshToken);
		redisTemplate.expire(refreshTokenKey, System.currentTimeMillis() + jwtTokenUtil.JWT_REFRESH_TOKEN_VALIDITY, TimeUnit.MILLISECONDS);
		
		System.out.println("Login Success");
		response.setStatus(HttpStatus.OK.value());
		
	}
	
	public void initJwtToken(String accessT, String refreshT) {
		ValueOperations<String,Object> redis = redisTemplate.opsForValue();
		if(redis.get(accessT) != null || redis.get(refreshT) != null) {
			redisTemplate.delete(accessT);
			redisTemplate.delete(refreshT);
		}
	}

}