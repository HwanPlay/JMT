package com.ssafy.videoconference.model.group.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.videoconference.controller.command.ChangeHostIdCommand;
import com.ssafy.videoconference.model.group.bean.Group;

public interface GroupRepository extends JpaRepository<Group, Integer>, GroupCustomRepository<Group> {


	
}
