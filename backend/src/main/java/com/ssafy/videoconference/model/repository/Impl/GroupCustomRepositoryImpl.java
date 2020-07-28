package com.ssafy.videoconference.model.repository.Impl;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.query.NativeQuery;

import com.ssafy.videoconference.model.bean.Group;
import com.ssafy.videoconference.model.repository.custom.GroupCustomRepository;

public class GroupCustomRepositoryImpl implements GroupCustomRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	
	@Override
	public Group findById(int group_no) {
		 NativeQuery<Group> query = (NativeQuery<Group>) em.createNativeQuery("from `group` where group_no = :group_no", Group.class);
		    query.setParameter("group_no", group_no);
		    return query.getSingleResult();
	}

}
