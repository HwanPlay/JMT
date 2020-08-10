package com.ssafy.videoconference.controller.result;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ssafy.videoconference.model.note.bean.Note;

import lombok.AllArgsConstructor;
import lombok.Data;

public class NoteResult {
	
	
	public static ResponseEntity<ApiResult> build(Note note, String groupName) {
		ApiResult apiResult = ApiResult.blank()
				.add("noteNo", note.getNoteNo())
				.add("groupName", groupName)
				.add("id", note.getUser().getId())
				.add("title", note.getTitle())
				.add("content", note.getContent())
				.add("meeting", note.getMeeting().getMeetingNo())
				.add("start_time", note.getMeeting().getCreatedDate())
				.add("end_time", note.getMeeting().getModifiedDate())
				.add("date", note.getCreatedDate());
		return Result.ok(apiResult);
	}
	
	
	public static ResponseEntity<ApiResult> build(Note note) {
		ApiResult apiResult = ApiResult.blank()
				.add("noteNo", note.getNoteNo())
				.add("groupNo", note.getGroup().getGroupNo())
				.add("id", note.getUser().getId())
				.add("title", note.getTitle())
				.add("content", note.getContent())
				.add("meeting", note.getMeeting().getMeetingNo())
				.add("start_time", note.getMeeting().getCreatedDate())
				.add("end_time", note.getMeeting().getModifiedDate())
				.add("date", note.getCreatedDate());
		return Result.ok(apiResult);
	}
	
	
	public static ResponseEntity<ApiResult> build(List<Note> notes) {
		ArrayList<NoteData> notesData = new ArrayList<>();
		for(Note note : notes) {
			notesData.add(new NoteData(note.getNoteNo(),
										note.getTitle(),
										note.getMeeting().getMeetingNo(),
										note.getCreatedDate()));
		}
		ApiResult apiResult = ApiResult.blank()
				.add("notes", notesData);
		return Result.ok(apiResult);
		
	}
	
	
	@Data
	@AllArgsConstructor
	private static class NoteData {
		private int noteNo;
		private String title;
		private int meetingNo;
		private LocalDateTime createdDate;
	}
}
