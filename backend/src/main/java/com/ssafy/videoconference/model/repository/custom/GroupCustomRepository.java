package com.ssafy.videoconference.model.repository.custom;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.videoconference.model.bean.Group;


@Transactional
public interface GroupCustomRepository<T> {
	
	Group findById(int groupNo);
	
	
	List<Group> findByHostId(String hostId);
	
	
	@Modifying
	void deleteByHostId(String hostId);
	
	
	@Modifying
	void deleteByNo(int groupNo);
	
	
//	Group add(Group group);
	
	
}