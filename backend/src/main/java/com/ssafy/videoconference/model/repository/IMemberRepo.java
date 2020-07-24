package com.ssafy.videoconference.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.videoconference.model.bean.Member;

public interface IMemberRepo extends JpaRepository<Member, Integer>{

}
