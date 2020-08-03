package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.ChangeTitleCommand;

import lombok.Data;

@Data
public class ChangeTitlePayload {
	
	private String title;
	
	public ChangeTitleCommand toCommand(int noteNo) {
		return new ChangeTitleCommand(noteNo, title);
	}
}
