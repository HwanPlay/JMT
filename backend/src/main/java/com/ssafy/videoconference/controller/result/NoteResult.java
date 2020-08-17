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
				.add("note_title", note.getTitle())
				.add("meeting_title", note.getMeeting().getTitle())
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
				.add("note_title", note.getTitle())
				.add("meeting_title", note.getMeeting().getTitle())
				.add("content", note.getContent())
				.add("meeting", note.getMeeting().getMeetingNo())
				.add("date", note.getCreatedDate());
		return Result.ok(apiResult);
	}
	
	
	public static ResponseEntity<ApiResult> build_add(Note note) {
		ApiResult apiResult = ApiResult.blank()
				.add("noteNo", note.getNoteNo())
				.add("groupNo", note.getGroup().getGroupNo())
				.add("id", note.getUser().getId())
				.add("note_title", note.getTitle())
				.add("meeting_title", note.getMeeting().getTitle())
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
										note.getGroup().getGroupNo(),
										note.getTitle(),
										note.getMeeting().getTitle(),
										note.getMeeting().getMeetingNo(),
										note.getCreatedDate(),
										note.getMeeting().getCreatedDate(),
										note.getMeeting().getModifiedDate()));
		}
		ApiResult apiResult = ApiResult.blank()
				.add("notes", notesData);
		return Result.ok(apiResult);
		
	}
	
	
	public static ResponseEntity<ApiResult> build_note(Note note) {		
		ApiResult apiResult = ApiResult.blank();
		apiResult.add("isNote", true).add("noteNo", note.getNoteNo());
		return Result.ok(apiResult);
	}
	
	
	public static ResponseEntity<ApiResult> build_error(boolean b) {
		ApiResult apiResult = ApiResult.blank().add("isNote", b);
		return Result.ok(apiResult);
	}
	
	
	public static ResponseEntity<ApiResult> build(int count) {
		ApiResult apiResult = ApiResult.blank()
				.add("count", count);
		return Result.ok(apiResult);
	}
	
	
	@Data
	@AllArgsConstructor
	private static class NoteData {
		private int noteNo;
		private int groupNo;
		private String note_title;
		private String meeting_title;
		private int meetingNo;
		private LocalDateTime createdDate;
		private LocalDateTime meeting_start_time;
		private LocalDateTime meeting_end_time;
	}
}