package com.ssafy.videoconference.config.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.ssafy.videoconference.model.user.bean.UserDetail;
import com.ssafy.videoconference.model.user.service.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtTokenUtil implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	private static final long serialVersionUID = -2550185165626007488L;
	public static final long JWT_ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000; // 1시간
//	public static final long JWT_ACCESS_TOKEN_VALIDITY = 2 * 5 * 1000; // 10초
	public static final long JWT_REFRESH_TOKEN_VALIDITY = 24 * 60 * 60 * 7 * 1000 * 2; // 2주일
//	public static final long JWT_REFRESH_TOKEN_VALIDITY = 2 * 10 * 1000; // 일주일
	
	@Value("${jwt.secretKey}")
	private String secretKey;

	@Resource(name = "userDetailsService")
	private UserDetailsService userDetailsService;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	// check if the token has expired
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	// AccessToken 생성
	public String generateAccessToken(UserDetail userDetail) {
		Map<String, Object> claims = new HashMap<>();
		List<String> li = new ArrayList<>();
		for (GrantedAuthority a : userDetail.getAuthorities()) {
			li.add(a.getAuthority());
		}
	//	claims.put("name", userDetail.getName());
		claims.put("role", li);
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userDetail.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY ))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	// RefreshToken 생성
	public String generateRefreshToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}

    public boolean isValidToken(String token, UserDetail userDetail) {
    	 final String username = getUsernameFromToken(token);
         return (username.equals(userDetail.getUsername()) && !isTokenExpired(token));
    }
    
//	public String getUserNameFromJwtToken(String token) {
//		getClaimFromToken(token, Claims::getSubject);
//		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//	}
    
//	public boolean isValidToken(String authToken) {
//		try {
//			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
//			return true;
//		} catch (SignatureException e) {
//			logger.error("Invalid JWT signature: {}", e.getMessage());
//		} catch (MalformedJwtException e) {
//			logger.error("Invalid JWT token: {}", e.getMessage());
//		} catch (ExpiredJwtException e) {
//			logger.error("JWT token is expired: {}", e.getMessage());
//		} catch (UnsupportedJwtException e) {
//			logger.error("JWT token is unsupported: {}", e.getMessage());
//		} catch (IllegalArgumentException e) {
//			logger.error("JWT claims string is empty: {}", e.getMessage());
//		}
//		return false;
//	}

//	public Authentication createAuthenticationFromToken(String token) {
//		UserDetails userDetails = ((UserDetailsServiceImpl) userDetailsService)
//				.loadUserByUsername(getUserIdFromToken(token));
//		// it is rather safe to return Authentication with NULL credentials if you do
//		// not require to use user credentials after successful authentication.
//		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//	}

//	public String getTokenFromHeader(String header) {
//		return header.split(" ")[1];
//	}

//	private Claims getClaimsFormToken(String token) {
//		return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token)
//				.getBody();
//	}
//
//	private String getUserIdFromToken(String token) {
//		Claims claims = getClaimsFormToken(token);
//		return (String) claims.get("id");
//	}

}
