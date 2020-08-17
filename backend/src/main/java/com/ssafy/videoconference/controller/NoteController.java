package com.ssafy.videoconference.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.videoconference.controller.command.ChangeContentCommand;
import com.ssafy.videoconference.controller.command.ChangeNoteCommand;
import com.ssafy.videoconference.controller.command.ChangeTitleCommand;
import com.ssafy.videoconference.controller.command.SaveNoteCommand;
import com.ssafy.videoconference.controller.payload.ChangeContentPayload;
import com.ssafy.videoconference.controller.payload.ChangeNotePayload;
import com.ssafy.videoconference.controller.payload.ChangeTitlePayload;
import com.ssafy.videoconference.controller.payload.SaveNotePayload;
import com.ssafy.videoconference.controller.result.ApiResult;
import com.ssafy.videoconference.controller.result.NoteResult;
import com.ssafy.videoconference.controller.result.Result;
import com.ssafy.videoconference.model.note.bean.Note;
import com.ssafy.videoconference.model.note.service.NoteService;
import com.ssafy.videoconference.model.user.bean.CurrentUser;
import com.ssafy.videoconference.model.user.bean.UserDetail;


@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/api/note")
public class NoteController {
	
	
	@Autowired
	private NoteService noteService;
	
	
	@PostMapping("/save")
	public ResponseEntity<ApiResult> saveNote(@RequestBody SaveNotePayload payload) {
		SaveNoteCommand command = payload.toCommand();
		Note note = noteService.saveNote(command);
		return NoteResult.build(note);
	}
	
	
	@PutMapping("/title/{noteNo}")
	public ResponseEntity<ApiResult> changeTitle(@PathVariable("noteNo") int noteNo,
													@RequestBody ChangeTitlePayload payload) {
		ChangeTitleCommand command = payload.toCommand(noteNo);
		noteService.changeTitle(command);
		return Result.ok();
	}
	
	
	@PutMapping("/content/{noteNo}")
	public ResponseEntity<ApiResult> changeContent(@PathVariable("noteNo") int noteNo,
													@RequestBody ChangeContentPayload payload) {
		ChangeContentCommand command = payload.toCommand(noteNo);
		noteService.changeContent(command);
		return Result.ok();
	}
	
	
	@PutMapping("/{noteNo}")
	public ResponseEntity<ApiResult> changeNote(@PathVariable("noteNo") int noteNo,
												@RequestBody ChangeNotePayload payload) {
		ChangeNoteCommand command = payload.toCommand(noteNo);
		noteService.changeNote(command);
		return Result.ok();
	}
	
	
	@DeleteMapping("/delno/{noteNo}/{groupNo}")
	public ResponseEntity<ApiResult> deleteByNo(@PathVariable("noteNo") int noteNo,
												@PathVariable("groupNo") int groupNo,
												@CurrentUser UserDetail user) {
		noteService.deleteByNo(noteNo);
		ArrayList<Note> note_list = (ArrayList<Note>) noteService.findByGroup(groupNo, user.getId());
		int count = note_list.size();
		return NoteResult.build(count);
	}	
	
	
	@GetMapping("/get/group/{groupNo}") 
	public ResponseEntity<ApiResult> getNoteByGroup(@PathVariable("groupNo") int groupNo, @CurrentUser UserDetail user) {
		List<Note> note_list = noteService.findByGroup(groupNo, user.getId());
		return NoteResult.build(note_list);
	}
	
	
	@GetMapping("/getno/{noteNo}")
	public ResponseEntity<ApiResult> getNoteByNo(@PathVariable("noteNo") int noteNo) {
		Note note = noteService.findByNo(noteNo);
		return NoteResult.build_add(note);
	}
	
	
	@GetMapping("/getno/{groupNo}/{meetingNo}")
	public ResponseEntity<ApiResult> getNoteNoByMeeting(@PathVariable("groupNo") int groupNo,
														@PathVariable("meetingNo") int meetingNo,
														@CurrentUser UserDetail user) {
		Note note;
		try {
			note = noteService.findByAll(groupNo, meetingNo, user.getId());
		} catch(Exception e) {
			return NoteResult.build_error(false);
		}
		return NoteResult.build_note(note);
	}
	
}