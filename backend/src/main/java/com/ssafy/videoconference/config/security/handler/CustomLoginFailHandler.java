package com.ssafy.videoconference.config.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomLoginFailHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException {
	// 로그인 실패 시, 400 에러
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    
//    ApiResult failure;
//    if (exception instanceof BadCredentialsException) {
//      failure = ApiResult.message("Invalid credentials");
//    } else if (exception instanceof InsufficientAuthenticationException) {
//      failure = ApiResult.message("Invalid authentication request");
//    } else {
//      failure = ApiResult.message("Authentication failure");
//    }
//    JsonUtils.write(response.getWriter(), failure);

  }
}
