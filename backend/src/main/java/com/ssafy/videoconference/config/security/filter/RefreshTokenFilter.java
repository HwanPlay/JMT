package com.ssafy.videoconference.config.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.videoconference.config.security.exception.JwtAuthEntryPoint;
import com.ssafy.videoconference.config.util.JwtTokenUtil;
import com.ssafy.videoconference.model.user.bean.UserDetail;
import com.ssafy.videoconference.model.user.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

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
		
//		final String token = request.getHeader("Authorization");
//		ValueOperations<String, Object> redis = redisTemplate.opsForValue();
//
//		String userId = null;
//		String accessToken = null;
//
//		if (token != null && token.startsWith("Bearer ")) {
//
//			accessToken = token.substring(7);
//
//			// AccessToken Claim(payload)에 저장된 userId 가져오기
//			// Claim으로 변환 도중 예외가 발생하면 유효하지 않은 토큰으로 판단
//			try {
//				userId = jwtTokenUtil.getUserNameFromJwtToken(accessToken);
//				if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//					// Redis의 AccessToken과 동일한지 확인
//					if (accessToken.equals(redis.get(userId + "_accessToken"))) {
//						// Access Token 확인 완료 후, SecurityCOntextHolder에 Authentication 저장
//						UserDetail userDetail = this.userDetailsService.loadUserByUsername(userId);
//						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//								userDetail, null, userDetail.getAuthorities());
//						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//					}
//				}
//				// ExpiredJwtException : 403 에러 (Refresh 요청)
//				// 그 외의 Exception : 401 에러 (재 로그인 요청)
//			} catch (ExpiredJwtException e) {
//				response.setStatus(HttpStatus.BAD_REQUEST.value());
//				response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
//				logger.error("JWT token is expired : {}", e.getMessage());
//			} catch(JwtException e) {
//				// SignatureException, MalformedJwtException, UnsupportedJwtException, IllegalArgumentException
//				logger.error("Invalid JWT : {}", e.getMessage());
//			}
//		} else {
//			logger.warn("JWT Token does not begin with Bearer String");
//		}

	}

}
