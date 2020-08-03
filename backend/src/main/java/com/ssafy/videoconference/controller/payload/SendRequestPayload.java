package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.SendRequestCommand;

import lombok.Data;

@Data
public class SendRequestPayload {
	
	
	private int groupNo;
	private String hostId;
	private String userId;
	
	
	public SendRequestCommand toCommand() {
		return new SendRequestCommand(groupNo, hostId, userId);
	}
	
}
