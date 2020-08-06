package com.ssafy.videoconference.model.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.videoconference.model.user.repository.IFileRepository;

@Service
public class FileServiceImpl implements IFileService{

	@Autowired
	IFileRepository fileRepo;
	
	@Override
	public void saveFile(MultipartFile multipartFile, String rootPath, String saveFileName) {
		fileRepo.saveFile(multipartFile, rootPath, saveFileName);
	}

	@Override
	public void readFile() {
		// TODO Auto-generated method stub
		
	}

}
