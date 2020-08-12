package com.ssafy.videoconference.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
public class WebSocketRequestController {
	
	
	private final SimpMessageSendingOperations messagingTemplate;
	
	
	@MessageMapping("/request")
	public void message(RequestMessage message) {
		System.out.println(message + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		messagingTemplate.convertAndSend("/send/request/" + message.getReceiver(), message);
	}
	
	
	
}