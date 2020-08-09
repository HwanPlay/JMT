package com.ssafy.videoconference.model.groupmember.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.videoconference.model.groupmember.bean.GroupMember;

public interface Group_memberRepository extends JpaRepository<GroupMember, Integer>, Group_memberCustomRepository{


}
