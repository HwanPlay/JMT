package com.ssafy.videoconference.model.meeting.service;

import java.util.List;

import com.ssafy.videoconference.controller.command.AddMeetingCommand;
import com.ssafy.videoconference.model.meeting.bean.Meeting;

public interface MeetingService {
	
	
	Meeting addMeeting(AddMeetingCommand command);
	
	
	Meeting findByNo(int meetingNo);
	
	
	List<Meeting> findByGroup(int groupNo);
}
