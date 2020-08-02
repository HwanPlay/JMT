package com.ssafy.videoconference.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.videoconference.model.bean.Group;


public interface GroupService {
	

	Group createGroup(Group group);
	
	
	Group findById(int groupNo);
	
	
	List<Group> findByHostId(String hostId);
	
	
	void deleteByHostId(String hostId);
	
	
	void deleteByNo(int groupNo);
	
	
//	void update(Group group); 


}