package com.ssafy.videoconference.model.meeting.repository;

import java.util.List;

import com.ssafy.videoconference.model.meeting.bean.Meeting;

public interface MeetingCustomRepository<T> {
	
	
	Meeting findByNo(int meetingNo);
	
	List<Meeting> findByGroup(int groupNo);
	
	void deleteByGroup(int groupNo);
	
		
}