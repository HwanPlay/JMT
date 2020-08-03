package com.ssafy.videoconference.config.util;

import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.ssafy.videoconference.model.user.bean.User;
import com.ssafy.videoconference.model.user.bean.UserDetail;
import com.ssafy.videoconference.model.user.service.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtTokenUtil implements Serializable{
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_ACCESS_TOKEN_VALIDITY = 30 * 60; //30분
    public static final long JWT_REFRESH_TOKEN_VALIDITY = 24 * 60 * 60 * 7; //일주일
    
    @Value("${jwt.secretKey}")
    private String secretKey;
    
    @Resource(name = "userDetailsService")
    private UserDetailsService userDetailsService;
    
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
    
    
    //generate token for user
    public String generateAccessToken(UserDetail userDetail) {
        Map<String, Object> claims = new HashMap<>();
        List<String> li = new ArrayList<>();
        for (GrantedAuthority a: userDetail.getAuthorities()) {
            li.add(a.getAuthority());
        }
        claims.put("role",li);
        return Jwts.builder().setClaims(claims).setSubject(userDetail.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }
    

    public boolean isValidToken(String token, UserDetail userDetail) {
    	 final String username = getUsernameFromToken(token);
         return (username.equals(userDetail.getUsername()) && !isTokenExpired(token));
    }
    
    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Authentication createAuthenticationFromToken(String token) {
        UserDetails userDetails = ((UserDetailsServiceImpl) userDetailsService).loadUserByUsername(getUserIdFromToken(token));
        // it is rather safe to return Authentication with NULL credentials if you do not require to use user credentials after successful authentication.
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    private Claims getClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token).getBody();
    }

    private String getUserIdFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return (String) claims.get("id");
    }

}
