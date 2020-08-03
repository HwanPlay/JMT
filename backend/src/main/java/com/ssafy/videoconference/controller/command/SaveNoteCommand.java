package com.ssafy.videoconference.controller.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveNoteCommand {
	
	private int groupNo;
	private String id;
	private String title;
	private String content;
	private int meetingNo;
	
}
