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
    RedisTemplate<String,Object> redisTemplate;
	
	private final JwtTokenUtil jwtTokenUtil;

	public JwtAuthorizationFilter(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// Access Token의 Authorization
		// 0. 사용자가 보낸 accessToken의 payload에 저장된 userId 확인하기
		// 1. 확인한 userId로 Redis에 저장된 accessToken key 찾기 - 있어야 함
		// 2. 사용자가 보낸 accessToken이랑 Redis에 저장된 accessToken이 동일한지 확인 - 동일해야 함!
		// 3. accessToken의 만료기간 확인하기 - 현재 날짜와 비교. 만료되기 1분 전이라면, exception 
		// 4. 전부 확인했으면, 다음 filter 실행 (성공-요청 api 실행)
		
		final String accessToken = request.getHeader("Authorization");

		String username = null;

		String jwtToken = null;

		if (accessToken != null && accessToken.startsWith("Bearer ")) {

			jwtToken = accessToken.substring(7);
			try {
				// AccessToken payload에 저장된 userId 가져오기
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);

			} catch (IllegalArgumentException e) {
				logger.error("JWT claims string is empty: {}", e.getMessage());

			} catch (ExpiredJwtException e) {
				logger.error("JWT token is expired: {}", e.getMessage());
			}

		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		ValueOperations<String,Object> redis = redisTemplate.opsForValue();

		
		redis.get(username+"_accessToken");
		UserDetail userDetail = this.userDetailsService.loadUserByUsername(username);
		// AccessToken 만료 여부 확인
		if (jwtTokenUtil.isValidToken(jwtToken, userDetail) && ) {
			
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
		//	usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
		chain.doFilter(request, response);
	}

}
