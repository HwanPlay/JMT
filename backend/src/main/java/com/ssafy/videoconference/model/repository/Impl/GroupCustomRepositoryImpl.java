package com.ssafy.videoconference.model.repository.Impl;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


import com.ssafy.videoconference.model.bean.Group;
import com.ssafy.videoconference.model.repository.custom.GroupCustomRepository;

public class GroupCustomRepositoryImpl implements GroupCustomRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public Group findById(int group_no) {
		String jpql = 
				" select * from conference where group_no = :group_no ";
		TypedQuery<Group> query = em.createQuery(jpql, Group.class);
		query.setParameter("group_no", group_no);
		return query.getSingleResult();
	}
	
}
