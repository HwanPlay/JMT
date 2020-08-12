package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.ChangeGroupAllCommand;

import lombok.Data;

@Data
public class ChangeGroupAllPayload {
	
	private String groupName;
	private String groupIntro;
	
	public ChangeGroupAllCommand toCommand(int groupNo) {
		return new ChangeGroupAllCommand(groupNo, groupName, groupIntro);
	}

}
