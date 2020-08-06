package com.ssafy.videoconference.model.user.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

	public void saveFile(MultipartFile multipartFile, String rootPath, String saveFileName);
	public void readFile(); 
}
