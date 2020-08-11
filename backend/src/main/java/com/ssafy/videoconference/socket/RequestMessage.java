package com.ssafy.videoconference.socket;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMessage {
	
	private String receiver;
	private int groupNo;
	
}