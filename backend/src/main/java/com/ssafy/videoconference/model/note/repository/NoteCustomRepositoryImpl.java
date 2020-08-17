package com.ssafy.videoconference.model.note.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.query.NativeQuery;

import com.ssafy.videoconference.model.note.bean.Note;

public class NoteCustomRepositoryImpl implements NoteCustomRepository {

	
	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public void deleteByNo(int noteNo) {
		String jpql =
				"delete from note where note_no = :noteNo";
		NativeQuery<Note> query = (NativeQuery<Note>) em.createNativeQuery(jpql, Note.class);
		query.setParameter("noteNo", noteNo);
		query.executeUpdate();
	}

	
	@Override
	public List findByGroup(int groupNo, String id) {
		String jpql =
				"select * from note where group_no = :groupNo and id = :id order by created_date asc";
		NativeQuery<Note> query = (NativeQuery<Note>) em.createNativeQuery(jpql, Note.class);
		query.setParameter("groupNo",  groupNo);
		query.setParameter("id",  id);
		return query.getResultList();
	}

	
	@Override
	public Note findByNo(int noteNo) {
		String jpql =
				"select * from note where note_no = :noteNo";
		NativeQuery<Note> query = (NativeQuery<Note>) em.createNativeQuery(jpql, Note.class);
		query.setParameter("noteNo", noteNo);
		return query.getSingleResult();
	}


	@Override
	public void deleteByGroup(int groupNo) {
		String jpql =
				"delete from note where group_no = :groupNo";
		NativeQuery<Note> query = (NativeQuery<Note>) em.createNativeQuery(jpql, Note.class);
		query.setParameter("groupNo", groupNo);
		query.executeUpdate(); 
	}


	@Override
	public Note findByAll(int groupNo, int meetingNo, String id) {
		String jpql =
				"select * from note where group_no = :groupNo and meeting_no = :meetingNo, id = :id";
		NativeQuery<Note> query = (NativeQuery<Note>) em.createNativeQuery(jpql, Note.class);
		query.setParameter("groupNo", groupNo);
		query.setParameter("meetingNo", meetingNo);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	
}
