package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.AddMemberCommand;

import lombok.Data;

@Data
public class AddMemberPayload {
	
	private int groupNo;
	private String id;
	private String nickname;
	
	public AddMemberCommand toCommand() {
		return new AddMemberCommand(groupNo, id, nickname);
	}

}
