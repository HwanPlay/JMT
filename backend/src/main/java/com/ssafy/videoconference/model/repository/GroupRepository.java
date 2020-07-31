package com.ssafy.videoconference.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.videoconference.model.bean.Group;
import com.ssafy.videoconference.model.repository.custom.GroupCustomRepository;

public interface GroupRepository extends JpaRepository<Group, Integer>, GroupCustomRepository<Group> {
	
}
