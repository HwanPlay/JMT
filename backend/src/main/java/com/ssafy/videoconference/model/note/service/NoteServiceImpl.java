package com.ssafy.videoconference.model.note.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.videoconference.controller.command.ChangeContentCommand;
import com.ssafy.videoconference.controller.command.ChangeNoteCommand;
import com.ssafy.videoconference.controller.command.ChangeTitleCommand;
import com.ssafy.videoconference.controller.command.SaveNoteCommand;
import com.ssafy.videoconference.model.note.bean.Note;
import com.ssafy.videoconference.model.note.repository.NoteRepository;


@Service
@Transactional
public class NoteServiceImpl implements NoteService {

	
	@Autowired
	private NoteRepository noteRepository;

	@Override
	public Note saveNote(SaveNoteCommand command) {
		Note note = Note.create(command.getGroupNo(), command.getId(), command.getTitle()
								, command.getContent(), command.getMeetingNo());
		noteRepository.save(note);
		return note;
	}

	@Override
	public void changeTitle(ChangeTitleCommand command) {
		Note note = findByNo(command.getNoteNo());
		note.setTitle(command.getTitle());
		noteRepository.save(note);
	}

	@Override
	public void changeContent(ChangeContentCommand command) {
		Note note = findByNo(command.getNoteNo());
		note.setContent(command.getContent());
		noteRepository.save(note);
	}

	@Override
	public void deleteByNo(int noteNo) {
		noteRepository.deleteByNo(noteNo);
	}

	@Override
	public List<Note> findByGroup(int groupNo, String id) {
		return noteRepository.findByGroup(groupNo, id);
	}

	@Override
	public Note findByNo(int noteNo) {
		return noteRepository.findByNo(noteNo);
	}

	@Override
	public void changeNote(ChangeNoteCommand command) {
		Note note = findByNo(command.getNoteNo());
		note.setTitle(command.getTitle());
		note.setContent(command.getContent());
		noteRepository.save(note);
	}

	@Override
	public void deleteByGroup(int groupNo) {
		noteRepository.deleteByGroup(groupNo);
	}

		
	@Override
	public int countByGroup(int groupNo) {
		int count = noteRepository.countNoteByGroup(groupNo);
		return count;
	}
	
	
}
