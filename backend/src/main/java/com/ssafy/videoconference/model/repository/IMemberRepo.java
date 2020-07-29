package com.ssafy.videoconference.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.videoconference.model.bean.User;

public interface IMemberRepo extends JpaRepository<User, Integer>{

}
