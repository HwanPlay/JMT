package com.ssafy.videoconference.model.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindUser {
	private String id;
	private String name;
	private String profile_img;

}
