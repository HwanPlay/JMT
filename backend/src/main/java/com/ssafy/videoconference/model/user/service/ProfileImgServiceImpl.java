package com.ssafy.videoconference.model.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.videoconference.model.user.repository.IProfileImgRepository;

@Service
public class ProfileImgServiceImpl implements IProfileImgService{

	@Autowired
	IProfileImgRepository profileImgRepo;
	
	@Override
	public void saveFile(MultipartFile multipartFile, String rootPath, String saveFileName) {
		profileImgRepo.saveFile(multipartFile, rootPath, saveFileName);
	}

	@Override
	public void readFile() {
		// TODO Auto-generated method stub
		
	}

}
