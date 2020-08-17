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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.videoconference.config.util.JwtTokenUtil;
import com.ssafy.videoconference.model.user.bean.UserDetail;
import com.ssafy.videoconference.model.user.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

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
		System.out.println("Jwt Authorization Filter - start");
		// SecurityContextHolder.getContext().getAuthentication();

		// Access Token의 Authorization
		// 0. 사용자가 보낸 accessToken이랑 Redis에 저장된 accessToken이 동일한지 확인 (유효함도 함께 확인)
		// 1. Token의 payload(Claims)로 권한정보까지 만들어 인증처리된 Authentication 생성
		// 2. 생성한 Authentication을 SecurityContextHolder에 저장
		// 3. 전부 확인했으면, 다음 filter 실행 - doFilter 호출

		final String token = request.getHeader("Authorization");
		ValueOperations<String, Object> redis = redisTemplate.opsForValue();

		String userId = null;
		String accessToken = null;
		System.out.println("token : " + token);
		if (token != null && token.startsWith("Bearer ")) {
			accessToken = token.substring(7);
			// AccessToken Claim(payload)에 저장된 userId 가져오기
			// Claim으로 변환 도중 예외가 발생하면 유효하지 않은 토큰으로 판단
			System.out.println("만료됐을때, accessToken : " + redis.get(accessToken));
			userId = (String) redis.get(accessToken);
			System.out.println("userID" + userId);
			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// Access Token 확인 완료 후, SecurityCOntextHolder에 Authentication 저장
				UserDetail userDetail = this.userDetailsService.loadUserByUsername(userId);
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetail, null, userDetail.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			} else {
				logger.error("JWT token is expired");
				throw new AuthenticationException("Incorrect Access Token - Requested token does not match the token stored in the DB.");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		chain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.startsWith("/api/jwt/refresh");
	}

}
