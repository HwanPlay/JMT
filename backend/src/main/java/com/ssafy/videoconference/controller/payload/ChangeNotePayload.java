package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.ChangeNoteCommand;

import lombok.Data;

@Data
public class ChangeNotePayload {
	
	private String title;
	private String content;
	
	
	public ChangeNoteCommand toCommand(int noteNo) {
		return new ChangeNoteCommand(noteNo, title, content);
	}

}
