package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.CreateGroupCommand;

import lombok.Data;

@Data
public class CreateGroupPayload {
	
	private String hostId;
	private String groupName;
	private String groupIntro;
	
	public CreateGroupCommand toCommand() {
		return new CreateGroupCommand(hostId, groupName, groupIntro);
	}
		
}
