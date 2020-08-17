package com.ssafy.videoconference.controller.result;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ssafy.videoconference.model.meeting.bean.Meeting;
import com.ssafy.videoconference.model.note.bean.Note;

import lombok.AllArgsConstructor;
import lombok.Data;

public class MeetingResult {
	
	
	public static ResponseEntity<ApiResult> build(Meeting meeting) {
		ApiResult apiResult = ApiResult.blank()
				.add("meetingNo", meeting.getMeetingNo())
				.add("groupNo", meeting.getGroup().getGroupNo())
				.add("title", meeting.getTitle())
				.add("create_date", meeting.getCreatedDate())
				.add("modified_date", meeting.getModifiedDate());
		return Result.ok(apiResult);
	}
	
	
	
	public static ResponseEntity<ApiResult> build(List<Meeting> meetings) {
		ArrayList<MeetingData> meetingsData = new ArrayList<>();
		for(Meeting meeting : meetings) {
			meetingsData.add(new MeetingData(meeting.getMeetingNo(),
												meeting.getGroup().getGroupNo(),
												meeting.getTitle(),
												meeting.getCreatedDate(),
												meeting.getModifiedDate()));
		}
		ApiResult apiResult = ApiResult.blank()
				.add("meetings",  meetingsData);
		return Result.ok(apiResult);
	}
	
	
	
	public static ResponseEntity<ApiResult> build(int meetingNo) {
		ApiResult apiResult = ApiResult.blank()
				.add("meetingNo", meetingNo);
		return Result.ok(apiResult);
	}
	
	
	@Data
	@AllArgsConstructor
	private static class MeetingData {
		private int meetingNo;
		private int groupNo;
		private String title;
		private LocalDateTime createdDate;
		private LocalDateTime modifiedDate;
	}
}
