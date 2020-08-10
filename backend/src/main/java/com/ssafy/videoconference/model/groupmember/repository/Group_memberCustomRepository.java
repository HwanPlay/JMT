package com.ssafy.videoconference.model.groupmember.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ssafy.videoconference.model.groupmember.bean.GroupMember;

@Transactional
public interface Group_memberCustomRepository {
	
	
	GroupMember findMember(int groupNo, String id);
	
	
	GroupMember findById(int groupMemberNo);
	
	
	List<GroupMember> findByGroupNo(int groupNo);
	
	
	void deleteById(int groupNo, String id);
	
	
	void deleteByGroup(int groupNo);
	

}
