package com.ssafy.videoconference.model.note.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.videoconference.model.note.bean.Note;

public interface NoteRepository extends JpaRepository<Note, Integer>, NoteCustomRepository<Note> {


}
