package com.ssafy.videoconference.config.security;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ssafy.videoconference.model.user.bean.UserDetail;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource(name = "userDetailsService")
    private UserDetailsService userDetailsService;
    @NonNull
    private BCryptPasswordEncoder passwordEncoder;

	@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        
        // AuthenticaionFilter에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
        String userId = token.getName();
        String userPw = (String) token.getCredentials();

        System.out.println("provider : " + userId + " " + userPw);
        // UserDetailsService를 통해 DB에서 아이디로 사용자 조회
        UserDetail userDetail = (UserDetail) userDetailsService.loadUserByUsername(userId);
        
        if (!passwordEncoder.matches(userPw, userDetail.getPassword())) {
            throw new BadCredentialsException(userDetail.getUsername() + "Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetail, userPw, userDetail.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
