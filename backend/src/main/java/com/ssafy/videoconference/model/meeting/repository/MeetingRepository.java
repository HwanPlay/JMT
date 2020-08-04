package com.ssafy.videoconference.model.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.videoconference.model.meeting.bean.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Integer>, MeetingCustomRepository<Meeting>{

}
