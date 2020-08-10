package com.ssafy.videoconference.model.groupmember.service;

import java.util.List;

import com.ssafy.videoconference.controller.command.AddMemberCommand;
import com.ssafy.videoconference.controller.command.ChangeNicknameCommand;
import com.ssafy.videoconference.model.groupmember.bean.GroupMember;

public interface Group_memberService {

	
	GroupMember addMember(AddMemberCommand command);
	
	
	GroupMember findMember(int groupNo, String id);
	
	
	GroupMember findById(int groupMemberNo);
	
	
	List<GroupMember> findByNo(int groupNo);
	
	
	void deleteById(int groupNo, String id);
	
	
	void changeNickname(ChangeNicknameCommand command);
	
	
	void deleteByGroup(int groupNo);
	

}
