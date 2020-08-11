package com.ssafy.videoconference.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class WebSocketMeetingController {
	
	private final SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("/meeting")
	public void message(MeetingMessage message) {
		System.err.println("!!@!@!@!@#!@#!@$#@$@!%#$^$#^%$&#%^@#$%!@#$!@#$!@^#$@^@#%@$@!#");
		messagingTemplate.convertAndSend("/sub/meeting" + message.getGroupNo(), message);
	}
}