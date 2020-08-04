package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.ChangeContentCommand;

import lombok.Data;


@Data
public class ChangeContentPayload {
	
	
private String content;
	
	public ChangeContentCommand toCommand(int noteNo) {
		return new ChangeContentCommand(noteNo, content);
	}
}
