package com.ssafy.videoconference.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.ssafy.videoconference.config.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	
//	@Override
//	public Message<?> preSend(Message<?> message, MessageChannel channel) {
//		// TODO Auto-generated method stub
//		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//		if(StompCommand.CONNECT == accessor.getCommand()) {
//			jwtTokenUtil.isValidToken(token, userDetail)
//		}
//		return ChannelInterceptor.super.preSend(message, channel);
//	}

}
