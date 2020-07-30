package com.ssafy.videoconference.model.repository.custom;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.query.NativeQuery;

import com.ssafy.videoconference.model.bean.Group;


public class GroupCustomRepositoryImpl implements GroupCustomRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public Group findById(int groupNo) {
		String jpql = 
				"select c.* from conference c where group_no = :groupNo ";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("groupNo", groupNo);
		return query.getSingleResult();
	}


	@Override
	public List<Group> findByHostId(String hostId) {
		// TODO Auto-generated method stub
		String jpql =
				"select * from conference where host_id = :hostId ";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("hostId", hostId);
		return query.getResultList();
	}


	@Override
	public void deleteByHostId(String hostId) {
		// TODO Auto-generated method stub
		String jpql =
				"delete from conference where host_id = :hostId ";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("hostId", hostId);
		query.executeUpdate();
	}


	@Override
	public void deleteByNo(int groupNo) {
		// TODO Auto-generated method stub
		String jpql =
				"delete from conference where group_no = :groupNo ";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("groupNo", groupNo);
		query.executeUpdate();
	}


//	@Override
//	public Group add(Group group) {
//		// TODO Auto-generated method stub
//		String jpql =
//				"insert into conference (host_id, group_name) value (:hostId, :groupName)";
//		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
//		query.setParameter("hostId", group.getHostId());
//		query.setParameter("groupName", group.getGroupName());
//		query.executeUpdate();
//		return group;
//	}

	
	@Override
	public Group update(Group group) {
		// TODO Auto-generated method stub
		String jpql =
				"update conference c set c.host_id = :hostId, c.group_name = :groupName where c.group_no = :groupNo";
		NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery(jpql, Group.class);
		query.setParameter("hostId", group.getHostId());
		query.setParameter("groupName",  group.getGroupName());
		query.setParameter("groupNo", group.getGroupNo());
		query.executeUpdate();
		return group;
	}

	
}
