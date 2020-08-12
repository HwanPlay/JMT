package com.ssafy.videoconference.model.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyUserPw {
	private String oldPw;
	private String newPw;
	
}
