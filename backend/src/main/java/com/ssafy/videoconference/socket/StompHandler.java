package com.ssafy.videoconference.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.ssafy.videoconference.config.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		// TODO Auto-generated method stub
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		if(StompCommand.CONNECT == accessor.getCommand()) {
			try {
				jwtTokenUtil.isTokenExpired(accessor.getFirstNativeHeader("token").substring(7));
			} catch (ExpiredJwtException e) {
				System.out.println("토큰 만료됬음!!!!!!");
			} catch(Exception e) {
				System.out.println("뭔가 에러 떴음!!!!!!");
			}
		}
		System.out.println(message + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		return message;
	}

}
