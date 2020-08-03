package com.ssafy.videoconference.controller;

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
import com.ssafy.videoconference.controller.command.ChangeTitleCommand;
import com.ssafy.videoconference.controller.command.SaveNoteCommand;
import com.ssafy.videoconference.controller.payload.ChangeContentPayload;
import com.ssafy.videoconference.controller.payload.ChangeTitlePayload;
import com.ssafy.videoconference.controller.payload.SaveNotePayload;
import com.ssafy.videoconference.controller.result.ApiResult;
import com.ssafy.videoconference.controller.result.Result;
import com.ssafy.videoconference.model.note.bean.Note;
import com.ssafy.videoconference.model.note.service.NoteService;
import com.ssafy.videoconference.model.user.bean.CurrentUser;
import com.ssafy.videoconference.model.user.bean.User;


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
	
	
	@PutMapping("/contenet/{noteNo}")
	public ResponseEntity<ApiResult> changeContent(@PathVariable("noteNo") int noteNo,
													@RequestBody ChangeContentPayload payload) {
		ChangeContentCommand command = payload.toCommand(noteNo);
		noteService.changeConent(command);
		return Result.ok();
	}
	
	
	@DeleteMapping("/delno/{noteNo}")
	public ResponseEntity<ApiResult> deleteByNo(@PathVariable("noteNo") int noteNo) {
		noteService.deleteByNo(noteNo);
		return Result.ok();
	}
	
	
	@GetMapping("/get/group/{groupNo}") 
	public ResponseEntity<ApiResult> getNoteByGroup(@PathVariable("groupNo") int groupNo,
														@CurrentUser User currentUser) {
		List<Note> note_list = noteService.findByGroup(groupNo, currentUser.getId());
		return NoteResult.build(note_list);
	}
	
	
	@GetMapping("/getno/{noteNo}")
	public ResponseEntity<ApiResult> getNoteByNo(@PathVariable("noteNo") int noteNo) {
		Note note = noteService.getNoteByNo(noteNo);
		return NoteResult.build(note);
	}
	
}