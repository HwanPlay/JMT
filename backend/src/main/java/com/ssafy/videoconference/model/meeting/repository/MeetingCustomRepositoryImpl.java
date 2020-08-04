package com.ssafy.videoconference.model.meeting.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.query.NativeQuery;

import com.ssafy.videoconference.model.meeting.bean.Meeting;

public class MeetingCustomRepositoryImpl implements MeetingCustomRepository{

	
	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public Meeting findByNo(int meetingNo) {
		String jpql =
				"select * from meeting where meeting_no = :meetingNo";
		NativeQuery<Meeting> query = (NativeQuery<Meeting>) em.createNativeQuery(jpql, Meeting.class);
		query.setParameter("meetingNo",  meetingNo);
		return query.getSingleResult();
	}
	

	@Override
	public List findByGroup(int groupNo) {
		String jpql =
				"select * from meeting where group_no = :groupNo";
		NativeQuery<Meeting> query = (NativeQuery<Meeting>) em.createNativeQuery(jpql, Meeting.class);
		query.setParameter("groupNo",  groupNo);
		return query.getResultList();
	}

}
