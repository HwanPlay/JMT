package com.ssafy.videoconference.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class WebSocketConferenceControlelr {

	
	private final SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("/conference")
	public void message(ConferenceMessage message) {
		messagingTemplate.convertAndSend("/send/conference/" + message.getMeetingNo(), message);
	}
}
