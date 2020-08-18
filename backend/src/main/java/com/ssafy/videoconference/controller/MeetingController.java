package com.ssafy.videoconference.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.videoconference.controller.command.AddMeetingCommand;
import com.ssafy.videoconference.controller.payload.AddMeetingPayload;
import com.ssafy.videoconference.controller.result.ApiResult;
import com.ssafy.videoconference.controller.result.MeetingResult;
import com.ssafy.videoconference.controller.result.Result;
import com.ssafy.videoconference.model.meeting.bean.Meeting;
import com.ssafy.videoconference.model.meeting.service.MeetingService;
import com.ssafy.videoconference.model.note.service.NoteService;


@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/api/meeting")
public class MeetingController {
	
	
	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private NoteService noteService;
	
	
	@PostMapping("/add")
	public ResponseEntity<ApiResult> addMeetingLog(@RequestBody AddMeetingPayload payload) {
		AddMeetingCommand command = payload.toCommand();
		Meeting meeting = meetingService.addMeeting(command);
		return MeetingResult.build(meeting);
	}
	
	
	@GetMapping("/getno/{meetingNo}")
	public ResponseEntity<ApiResult> getMeetingLogByNo(@PathVariable("meetingNo") int meetingNo) {
		Meeting meeting = meetingService.findByNo(meetingNo);
		return MeetingResult.build(meeting);
	}
	
	
	@GetMapping("/get/group/{groupNo}")
	public ResponseEntity<ApiResult> getMeetingLogByGroup(@PathVariable("groupNo") int groupNo) {
		List<Meeting> meeting_list = meetingService.findByGroup(groupNo);
		return MeetingResult.build(meeting_list);
	}
	
	
	@GetMapping("get/currentmeeting/{groupNo}")
	public ResponseEntity<ApiResult> getCurrentMeeting(@PathVariable("groupNo") int groupNo) {
		int meeting = meetingService.findCurrentMeeting(groupNo);
		return MeetingResult.build(meeting);
	}
	
	
	@PutMapping("update/{meetingNo}")
	public ResponseEntity<ApiResult> UpdateMeetingTime(@PathVariable("meetingNo") int meetingNo) {
		meetingService.updateMeetingTime(meetingNo);
		return Result.ok();
	}
	
	
}