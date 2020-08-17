package com.ssafy.videoconference.controller;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.videoconference.config.util.JwtTokenUtil;
import com.ssafy.videoconference.controller.result.ApiResult;
import com.ssafy.videoconference.controller.result.Result;
import com.ssafy.videoconference.model.user.bean.UserDetail;
import com.ssafy.videoconference.model.user.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.ApiOperation;


@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/api/jwt")
public class RefreshController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	@ApiOperation(value = "만료된 Access Token 요청. Refresh Token 확인 및 발급")
	@GetMapping("/refresh")
	public ResponseEntity<ApiResult> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 0. 만료된 access 토큰에서 userId 가져오기
		// 1. redis db에 refresh 토큰 있는지 확인(만료 확인)
		// 2. refresh 토큰 만료 안됐으면, access 토큰 재발급 (header에 저장)
		// 3. refresh 토큰 만료 됐으면, 재로그인 요청
		System.out.println("refresh Token");
		
		String accessToken = request.getHeader("accessToken");
		String refreshToken = request.getHeader("refreshToken");
		System.out.println(accessToken);
		System.out.println(refreshToken);
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
				System.out.println("refresh Id : "  +  userId);
				// Redis DB에 있는 Refresh Token 만료 확인 및 사용자 Refresh Token과 비교
				if (refreshToken.equals(redis.get(userId + "_refreshToken"))) {
					System.out.println("refresh 완료");
					UserDetail userDetail = this.userDetailsService.loadUserByUsername(userId);
					
					// Access Token 재 발급
					String newAccessToken = jwtTokenUtil.generateAccessToken(userDetail);
					
					// header에 Access Token 등록
					response.addHeader("AccessToken", "Bearer " + newAccessToken);

					// Redis DB에 재 발급한 Access Token 저장 
					redisTemplate.opsForValue().set(newAccessToken, userId);
					redisTemplate.expire(newAccessToken, jwtTokenUtil.JWT_ACCESS_TOKEN_VALIDITY, TimeUnit.MILLISECONDS);

					response.setStatus(HttpStatus.OK.value());
					System.out.println("success : " + userId + " : " + newAccessToken);
					return Result.ok(SUCCESS);
				}else {
					// Refresh Token 만료시, 재 로그인 요청
//					response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "expiredRefresh");
//					throw new JwtException("Unauthorized - Expired Refresh Token.");
					return Result.forbidden();

				}
			} catch (Exception e) {
				// 그 외 exception은 재 로그인 요청
				
				throw new JwtException("Unauthorized");
			}
			System.out.println("end");
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		return Result.failure(FAIL);
	}

}
