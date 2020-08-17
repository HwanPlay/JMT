package com.ssafy.videoconference.model.note.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.ssafy.videoconference.model.note.bean.Note;

@Transactional
public interface NoteCustomRepository<T> {
	
	
	void deleteByNo(int noteNo);
	
	
	List<Note> findByGroup(int groupNo, String id);
	
	
	Note findByNo(int noteNo);
	
	
	void deleteByGroup(int groupNo);
	
	
	Note findByAll(int groupNo, int meetingNo, String id);
	

}
