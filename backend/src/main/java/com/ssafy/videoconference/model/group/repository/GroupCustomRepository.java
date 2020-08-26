package com.ssafy.videoconference.model.group.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.videoconference.controller.command.ChangeHostIdCommand;
import com.ssafy.videoconference.model.group.bean.Group;


@Transactional
public interface GroupCustomRepository<T> {
	
	Group findById(int groupNo);
	
	
	List<Group> findByHostId(String hostId);
	
	
	List<Group> findByUserId(String userId);
	
	
	@Modifying
	void deleteByHostId(String hostId);
	
	
	@Modifying
	void deleteByNo(int groupNo);
	
	
	@Modifying
	void changeHostId(ChangeHostIdCommand command);
	
	
	@Modifying
	void initMeeting(String id);
	
	
//	Group add(Group group);
	
	
}