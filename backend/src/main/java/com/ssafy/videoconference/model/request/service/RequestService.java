package com.ssafy.videoconference.model.request.service;

import java.util.List;

import com.ssafy.videoconference.controller.command.SendRequestCommand;
import com.ssafy.videoconference.model.request.bean.Request;

public interface RequestService {
	
	
	Request sendRequest(SendRequestCommand command);
	
	
	void deleteByNo(int requestNo);
	
	
	List<Request> findSendRequest(int groupNo, String hostId);
	
	
	List<Request> findReceiveRequest(String userId);
	
	
	Request findRequest(int groupNo, String hostId, String userId);
	
	
}
