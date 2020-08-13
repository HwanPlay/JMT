package com.ssafy.videoconference.socket;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMessage {
	
	private String sencer;
	private String receiver;
	private String groupName;
	private int groupNo;
	
}