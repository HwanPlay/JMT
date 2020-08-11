package com.ssafy.videoconference.model.user.bean;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyUser{
	private String id;
	private String name;
//	private String pw;
	private String profile_img;
	private MultipartFile multipartFile;

}
