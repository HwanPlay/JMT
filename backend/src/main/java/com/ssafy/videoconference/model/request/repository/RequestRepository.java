package com.ssafy.videoconference.model.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.videoconference.model.request.bean.Request;

public interface RequestRepository extends JpaRepository<Request, Integer>, RequestCustomRepository<Request> {
	
}
