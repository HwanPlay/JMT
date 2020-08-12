package com.ssafy.videoconference.controller.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeGroupAllCommand {
	
	private int groupNo;
	private String groupName;
	private String groupIntro;
}
