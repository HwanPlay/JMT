package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.AddMeetingCommand;

import lombok.Data;

@Data
public class AddMeetingPayload {
	
	private String title;
	private int groupNo;
	
	
	public AddMeetingCommand toCommand() {
		return new AddMeetingCommand(title, groupNo);
	}
}
