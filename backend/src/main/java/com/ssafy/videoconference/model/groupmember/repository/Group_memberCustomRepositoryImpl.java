package com.ssafy.videoconference.model.groupmember.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.query.NativeQuery;

import com.ssafy.videoconference.model.groupmember.bean.GroupMember;

public class Group_memberCustomRepositoryImpl implements Group_memberCustomRepository {

	
	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public GroupMember findById(int groupMemberNo) {
		String jpql = 
				"select * from group_member where group_member_no = :groupMemberNo";
		NativeQuery<GroupMember> query = (NativeQuery<GroupMember>) em.createNativeQuery(jpql, GroupMember.class);
		query.setParameter("groupMemberNo", groupMemberNo);
		return query.getSingleResult();
	}

	
	@Override
	public List<GroupMember> findByGroupNo(int groupNo) {
		String jpql =
				"select * from group_member where group_no = :groupNo";
		NativeQuery<GroupMember> query = (NativeQuery<GroupMember>) em.createNativeQuery(jpql, GroupMember.class);
		query.setParameter("groupNo", groupNo);
		return query.getResultList();
	}


	@Override
	public GroupMember findMember(int groupNo, String id) {
		String jpql = 
				"select * from group_member where group_no = :groupNo and id = :id";
		NativeQuery<GroupMember> query = (NativeQuery<GroupMember>) em.createNativeQuery(jpql, GroupMember.class);
		query.setParameter("groupNo", groupNo);
		query.setParameter("id", id);
		return query.getSingleResult();
	}


	@Override
	public void deleteById(int groupNo, String id) {
		String jpql =
				"delete from group_member where group_no = :groupNo and id = :id";
		NativeQuery<GroupMember> query = (NativeQuery<GroupMember>) em.createNativeQuery(jpql, GroupMember.class);
		query.setParameter("groupNo", groupNo);
		query.setParameter("id", id);
		query.executeUpdate();
	}

}
