package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.ChangeHostIdCommand;

import lombok.Data;

@Data
public class ChangeHostIdPayload {
	
	private String hostId;
	
	public ChangeHostIdCommand toCommand(int groupNo) {
		return new ChangeHostIdCommand(groupNo, hostId);
	}

}
