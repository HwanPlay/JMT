package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.ChangeNicknameCommand;

import lombok.Data;

@Data
public class ChangeNicknamePayload {
	
	private String nickname;
	
	public ChangeNicknameCommand toCommand(int groupMemberNo) {
		return new ChangeNicknameCommand(groupMemberNo, nickname);
	}
}
