package com.ssafy.videoconference.model.group.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.videoconference.controller.command.ChangeGroupAllCommand;
import com.ssafy.videoconference.controller.command.ChangeGroupNameCommand;
import com.ssafy.videoconference.controller.command.ChangeHostIdCommand;
import com.ssafy.videoconference.controller.command.ChangeIntroCommand;
import com.ssafy.videoconference.controller.command.CreateGroupCommand;
import com.ssafy.videoconference.model.group.bean.Group;
import com.ssafy.videoconference.model.group.repository.GroupRepository;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
	
	
	@Autowired
	private GroupRepository groupRepository;
	
	
	@Override
	public Group createGroup(CreateGroupCommand command) {
		Group group = Group.create(command.getHostId(), command.getGroupName(), command.getGroupIntro());
		groupRepository.save(group);
		return group;
	}

	
	@Override
	public Group findById(int groupNo) {
		// TODO Auto-generated method stub
		return groupRepository.findById(groupNo);
	}

	
	@Override
	public List<Group> findByHostId(String hostId) {
		// TODO Auto-generated method stub
		return groupRepository.findByHostId(hostId);
	}

	
	@Override
	public void deleteByHostId(String hostId) {
		// TODO Auto-generated method stub
		groupRepository.deleteByHostId(hostId);
	}

	
	@Override
	public void deleteByNo(int groupNo) {
		// TODO Auto-generated method stub
		groupRepository.deleteByNo(groupNo);
		
	}

	
	@Override
	public void changeHostId(ChangeHostIdCommand command) {
//		Group group = findById(command.getGroupNo());
//		group.getUser().setId(command.getHostId());
//		groupRepository.save(group);
		groupRepository.changeHostId(command);
	}
	
	
	@Override
	public void changeGroupName(ChangeGroupNameCommand command) {
		Group group = findById(command.getGroupNo());
		group.setGroupName(command.getGroupName());
		groupRepository.save(group);
	}
	
	
	@Override
	public void changeIntro(ChangeIntroCommand command) {
		Group group = findById(command.getGroupNo());
		group.setGroupName(command.getGroupIntro());
		groupRepository.save(group);
	}
	
	
	@Override
	public void changeHasMeeting(int groupNo) {
		Group group = findById(groupNo);
		group.setHasmeeting(!group.isHasmeeting());
	}


	@Override
	public List<Group> findByUserId(String userId) {
		return groupRepository.findByUserId(userId);
	}


	@Override
	public void changeAll(ChangeGroupAllCommand command) {
		Group group = findById(command.getGroupNo());
		group.setGroupName(command.getGroupName());
		group.setGroupIntro(command.getGroupIntro());
		groupRepository.save(group);
	}

}