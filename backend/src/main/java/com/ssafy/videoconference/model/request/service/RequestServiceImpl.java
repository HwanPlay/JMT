package com.ssafy.videoconference.model.request.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.videoconference.controller.command.SendRequestCommand;
import com.ssafy.videoconference.model.request.bean.Request;
import com.ssafy.videoconference.model.request.repository.RequestRepository;


@Service
@Transactional
public class RequestServiceImpl implements RequestService{

	
	@Autowired
	private RequestRepository reqRepository;
	
	
	
	@Override
	public Request sendRequest(SendRequestCommand command) {
		Request req = Request.create(command.getGroupNo(), command.getHostId(), command.getUserId(), command.getGroupName());
		reqRepository.save(req);
		return req;
	}

	
	@Override
	public void deleteByNo(int requestNo) {
		reqRepository.deleteByNo(requestNo);
	}

	
	@Override
	public List<Request> findSendRequest(int groupNo, String hostId) {
		return reqRepository.findSendRequest(groupNo, hostId);
	}

	
	@Override
	public List<Request> findReceiveRequest(String userId) {
		return reqRepository.findReceiveRequest(userId);
	}

	
	@Override
	public Request findRequest(int groupNo, String hostId, String userId) {
		return reqRepository.findRequest(groupNo, hostId, userId);
	}

}
