package com.ssafy.videoconference.model.repository.custom;

import com.ssafy.videoconference.model.bean.Group;

public interface GroupCustomRepository<Group> {
	
	Group findById(int group_no);
	
	
}