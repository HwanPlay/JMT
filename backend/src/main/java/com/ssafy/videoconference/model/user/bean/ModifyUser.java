package com.ssafy.videoconference.model.user.bean;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class ModifyUser extends User {
	private MultipartFile multipartFile;
}
