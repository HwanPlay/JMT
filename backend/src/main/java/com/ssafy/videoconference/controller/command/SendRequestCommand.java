package com.ssafy.videoconference.controller.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendRequestCommand {
	
	private int groupNo;
	private String hostId;
	private String userId;
}
