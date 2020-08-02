package com.ssafy.videoconference.model.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.videoconference.model.bean.Group;
import com.ssafy.videoconference.model.repository.GroupRepository;
import com.ssafy.videoconference.model.service.GroupService;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
	
	
	@Autowired
	private GroupRepository groupRepository;
	
	
	@Override
	public Group createGroup(Group group) {
//		Group group_save = groupRepository.add(group);
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


//	@Override
//	public void update(Group group) {
//		// TODO Auto-generated method stub
//		Group group_update = groupRepository.findById(group.getGroupNo());
//		group_update = group;
//		groupRepository.save(group_update);
//	}

}
