package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.ChangeGroupNameCommand;

import lombok.Data;


@Data
public class ChangeGroupNamePayload {
	
	private String groupName;
	
	public ChangeGroupNameCommand toCommand(int groupNo) {
		return new ChangeGroupNameCommand(groupNo, groupName);
	}
}
