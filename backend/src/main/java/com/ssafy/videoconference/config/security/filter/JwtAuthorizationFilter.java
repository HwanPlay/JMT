package com.ssafy.videoconference.config.security.filter;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;
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

import com.ssafy.videoconference.config.util.JwtTokenUtil;
import com.ssafy.videoconference.model.user.bean.UserDetail;
import com.ssafy.videoconference.model.user.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private final JwtTokenUtil jwtTokenUtil;

	public JwtAuthorizationFilter(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// SecurityContextHolder.getContext().getAuthentication();

		// Access Token의 Authorization
		// 0. 사용자가 보낸 accessToken 복호화하여 userId 찾기 - 만료기간 함께 확인(Exception 처리)
		// 1. 확인한 userId로 Redis에 저장된 accessToken key 찾기 - 있어야 함
		// 2. 사용자가 보낸 accessToken이랑 Redis에 저장된 accessToken이 동일한지 확인 - 동일해야 함!
		// 3. Token의 payload(Claims)로 권한정보까지 만들어 인증처리된 Authentication 생성
		// 4. 생성한 Authentication을 SecurityContextHolder에 저장
		// 4. 전부 확인했으면, 다음 filter 실행 - doFilter 호출

		final String token = request.getHeader("Authorization");
		ValueOperations<String, Object> redis = redisTemplate.opsForValue();

		String userId = null;
		String accessToken = null;

		if (token != null && token.startsWith("Bearer ")) {

			accessToken = token.substring(7);

			// AccessToken Claim(payload)에 저장된 userId 가져오기
			// Claim으로 변환 도중 예외가 발생하면 유효하지 않은 토큰으로 판단
			try {
				userId = jwtTokenUtil.getUserNameFromJwtToken(accessToken);
				if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					// Redis의 AccessToken과 동일한지 확인
					if (accessToken.equals(redis.get(userId + "_accessToken"))) {
						// Access Token 확인 완료 후, SecurityCOntextHolder에 Authentication 저장
						UserDetail userDetail = this.userDetailsService.loadUserByUsername(userId);
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetail, null, userDetail.getAuthorities());
						System.out.println("userDetail : "+ userDetail.toString());
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}else {
						throw new AuthenticationException("Incorrect Access Token - Requested token does not match the token stored in the DB.");
					}
				}
				// ExpiredJwtException : 403 에러 (Refresh 요청)
				// 그 외의 Exception : 401 에러 (재 로그인 요청)
			} catch (ExpiredJwtException e) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
				logger.error("JWT token is expired : {}", e.getMessage());
				
			} catch(Exception e) {
				// SignatureException, MalformedJwtException, UnsupportedJwtException, IllegalArgumentException 한 번에 처리
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				logger.error("Invalid JWT : {}", e.getMessage());
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		//	throw new AuthenticationException("JWT Token does not begin with Bearer String");
		}

		chain.doFilter(request, response);
	}

}
