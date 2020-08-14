package com.ssafy.videoconference.model.request.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ssafy.videoconference.model.request.bean.Request;

@Transactional
public interface RequestCustomRepository<T> {
	
	
	void deleteByNo(int requestNo);
	
	
	List<Request> findSendRequest(int groupNo, String hostId);
	
	
	List<Request> findReceiveRequest(String userId);
	
	
	Request findRequest(int groupNo, String hostId, String userId);
	
	
	void deleteByGroup(int groupNo);


	void deleteByAll(String userId, String hostId, int groupNo);
	
}
