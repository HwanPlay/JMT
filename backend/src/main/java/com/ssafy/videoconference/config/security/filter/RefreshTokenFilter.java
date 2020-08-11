package com.ssafy.videoconference.config.security.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.videoconference.config.security.exception.JwtAuthEntryPoint;
import com.ssafy.videoconference.config.util.JwtTokenUtil;
import com.ssafy.videoconference.model.user.bean.UserDetail;
import com.ssafy.videoconference.model.user.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;

public class RefreshTokenFilter extends OncePerRequestFilter{
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtAuthEntryPoint jwtAuthEntryPoint;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		// 0. 만료된 access 토큰에서 userId 가져오기
		// 1. redis db에 refresh 토큰 있는지 확인(만료 확인)
		// 2. refresh 토큰 만료 안됐으면, access 토큰 재발급 (header에 저장)
		// 3. refresh 토큰 만료 됐으면, 재로그인 요청
		System.out.println("refresh Token");
		
		String accessToken = request.getHeader("accessToken");
		String refreshToken = request.getHeader("refreshToken");
			
		String userId = null;
		
		ValueOperations<String, Object> redis = redisTemplate.opsForValue();
		
		if (accessToken != null && refreshToken != null && accessToken.startsWith("Bearer ") && refreshToken.startsWith("Bearer ")) {
			
			accessToken = accessToken.substring(7);
			refreshToken = refreshToken.substring(7);
			
			try {
				// Access Token으로 사용자 정보(userId) 추출
				userId = jwtTokenUtil.getUsernameFromToken(accessToken);
			} catch (ExpiredJwtException e) {
				
				logger.info("username from expired access token: " + userId);
				
				// expire된 Token에서도 사용자 정보를 가져올 수 있음!
				userId = e.getClaims().getSubject();
				
				// Redis DB에 있는 Refresh Token 만료 확인 및 사용자 Refresh Token과 비교
				if (refreshToken.equals(redis.get(userId + "_refreshToken"))) {
					UserDetail userDetail = this.userDetailsService.loadUserByUsername(userId);
					
					// Access Token 재 발급
					String newAccessToken = jwtTokenUtil.generateAccessToken(userDetail);
					
					// header에 Access Token 등록
					response.addHeader("AccessToken", "Bearer " + newAccessToken);

					String accessTokenKey = userDetail.getId()+"_accessToken";
					
					// Redis DB에 재 발급한 Access Token 저장 
					redisTemplate.opsForValue().set(accessTokenKey, newAccessToken);
					redisTemplate.expire(accessTokenKey, System.currentTimeMillis() + jwtTokenUtil.JWT_ACCESS_TOKEN_VALIDITY, TimeUnit.MILLISECONDS);

					response.setStatus(HttpStatus.OK.value());
					
				}else {
					// Refresh Token 만료시, 재 로그인 요청
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					throw new AuthenticationException("Unauthorized - Expired Refresh Token. Retry Login.");
				}
			} catch (Exception e) {
				// 그 외 exception은 재 로그인 요청
//				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				throw new AuthenticationException("Unauthorized - Retry Login.");
			}
			System.out.println("end");
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		throw new AuthenticationException("Unauthorized - Expired Refresh Token. Retry Login.");

	}

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/jwt/refresh");
    }
}
