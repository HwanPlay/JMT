package com.ssafy.videoconference.model.user.repository;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class FileRepositoryImpl implements IFileRepository {
	@Override
	public void saveFile(MultipartFile multipartFile, String rootPath, String savefileName){
		
		File dir = new File(rootPath);
		if(!dir.exists())
			dir.mkdirs();
		
		String saveFileName = multipartFile.getOriginalFilename();
		
		try {
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
