package com.ssafy.videoconference.model.user.repository;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class ProfileImgRepositoryImpl implements IProfileImgRepository {
	@Override
	public void saveFile(MultipartFile multipartFile, String rootPath, String saveFileName){
		System.out.println("================================");
		rootPath = "/home/jenkins/workspace/joinmeeting/backend/resources";
		saveFileName = "spsp.png";
		File dir = new File(rootPath);
		if(!dir.exists())
			dir.mkdirs();
		
		try {
			System.out.println(rootPath + " " + saveFileName);
			File file = new File(rootPath, saveFileName);
			multipartFile.transferTo(file);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void readFile() {
		
	}


}
