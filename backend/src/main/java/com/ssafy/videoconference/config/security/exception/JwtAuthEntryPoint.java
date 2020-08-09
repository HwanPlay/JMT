package com.ssafy.videoconference.config.security.exception;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint, Serializable  {

    private static final long serialVersionUID = -7858869558953243875L;

    // 권한이 없을 경우, 예외처리
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
    	
    	  response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}