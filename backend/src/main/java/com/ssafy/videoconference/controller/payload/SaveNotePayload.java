package com.ssafy.videoconference.controller.payload;

import com.ssafy.videoconference.controller.command.SaveNoteCommand;

import lombok.Data;

@Data
public class SaveNotePayload {


	private int groupNo;
	private String id;
	private String title;
	private String content;
	private int meetingNo;
	
	public SaveNoteCommand toCommand() {
		return new SaveNoteCommand(groupNo, id, title, content, meetingNo);
	}
}
