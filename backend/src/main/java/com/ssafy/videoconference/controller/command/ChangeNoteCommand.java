package com.ssafy.videoconference.controller.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeNoteCommand {
	
	private int noteNo;
	private String title;
	private String content;
	
}
