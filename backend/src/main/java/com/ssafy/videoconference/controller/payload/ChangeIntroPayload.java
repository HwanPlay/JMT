package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.ChangeIntroCommand;

import lombok.Data;

@Data
public class ChangeIntroPayload {
	
	private String groupIntro;
	
	public ChangeIntroCommand toCommand(int groupNo) {
		return new ChangeIntroCommand(groupNo, groupIntro);
	}

}
