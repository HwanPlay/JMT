package com.ssafy.videoconference.model.meeting.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.videoconference.controller.command.AddMeetingCommand;
import com.ssafy.videoconference.model.meeting.bean.Meeting;
import com.ssafy.videoconference.model.meeting.repository.MeetingRepository;


@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {
	
	
	@Autowired
	private MeetingRepository meetingRepository;
	

	@Override
	public Meeting addMeeting(AddMeetingCommand command) {
		Meeting meeting = Meeting.create(command.getGroupNo(), command.getTitle());
		meetingRepository.save(meeting);
		return meeting;
	}
	

	@Override
	public Meeting findByNo(int meetingNo) {
		return meetingRepository.findByNo(meetingNo);
	}

	
	@Override
	public List<Meeting> findByGroup(int groupNo) {
		return meetingRepository.findByGroup(groupNo);
	}


	@Override
	public void deleteByGroup(int groupNo) {
		meetingRepository.deleteByGroup(groupNo);
	}


	@Override
	public int findCurrentMeeting(int groupNo) {
		ArrayList<Meeting> meeting_list = (ArrayList<Meeting>) meetingRepository.findByGroup(groupNo);
		return meeting_list.get(meeting_list.size() - 1).getMeetingNo();
	}


	@Override
	public void updateMeetingTime(int meetingNo) {
		Meeting meeting = meetingRepository.findByNo(meetingNo);
		meetingRepository.save(meeting);
	}

	
}