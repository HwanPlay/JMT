package com.ssafy.videoconference.model.group.repository;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.query.NativeQuery;

import com.ssafy.videoconference.controller.command.ChangeHostIdCommand;
import com.ssafy.videoconference.model.group.bean.Group;


public class GroupCustomRepositoryImpl implements GroupCustomRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public Group findById(int groupNo) {
		String jpql = 
				"select c.* from meeting_group c where group_no = :groupNo ";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("groupNo", groupNo);
		return query.getSingleResult();
	}


	@Override
	public List<Group> findByHostId(String hostId) {
		// TODO Auto-generated method stub
		String jpql =
				"select * from meeting_group where id = :hostId ";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("hostId", hostId);
		return query.getResultList();
	}
	
	
	@Override
	public List<Group> findByUserId(String userId) {
		String jpql =
				"select * from meeting_group where group_no in (select gm.group_no from group_member gm where gm.id = :userId)";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("userId", userId);
		return query.getResultList();
	}


	@Override
	public void deleteByHostId(String hostId) {
		// TODO Auto-generated method stub
		String jpql =
				"delete from meeting_group where id = :hostId ";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("hostId", hostId);
		query.executeUpdate();
	}


	@Override
	public void deleteByNo(int groupNo) {
		// TODO Auto-generated method stub
		String jpql =
				"delete from meeting_group where group_no = :groupNo ";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("groupNo", groupNo);
		query.executeUpdate();
	}
	
	
	@Override
	public void changeHostId(ChangeHostIdCommand command) {
		String jpql = 
				"update meeting_group set id = :Id where group_no = :GroupNo";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("Id", command.getHostId());
		query.setParameter("GroupNo", command.getGroupNo());
		query.executeUpdate();
	}
	
		
}