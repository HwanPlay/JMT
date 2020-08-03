package com.ssafy.videoconference.model.groupmember.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.videoconference.controller.command.AddMemberCommand;
import com.ssafy.videoconference.controller.command.ChangeNicknameCommand;
import com.ssafy.videoconference.model.groupmember.bean.GroupMember;
import com.ssafy.videoconference.model.groupmember.repository.Group_memberRepository;

@Service
@Transactional
public class Group_memberServiceImpl implements Group_memberService{

	
	@Autowired
	private Group_memberRepository gmRepository;
	
	
	@Override
	public GroupMember addMember(AddMemberCommand command) {
		GroupMember gm = GroupMember.create(command.getGroupNo(), command.getId(), command.getNickname());
		gmRepository.save(gm);
		return gm;
	}


	@Override
	public GroupMember findMember(int groupNo, String id) {
		return gmRepository.findMember(groupNo, id);
	}


	@Override
	public GroupMember findById(int groupMemberNo) {
		return gmRepository.findById(groupMemberNo);
	}


	@Override
	public List<GroupMember> findByNo(int groupNo) {
		return gmRepository.findByGroupNo(groupNo);
	}


	@Override
	public void deleteById(int groupMemberNo) {
		gmRepository.deleteById(groupMemberNo);
		
	}


	@Override
	public void changeNickname(ChangeNicknameCommand command) {
		GroupMember gm = findById(command.getGroupMemberNo());
		gm.setNickname(command.getNickname());
		gmRepository.save(gm);
	}
	
}
