package com.ssafy.videoconference.model.request.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.query.NativeQuery;

import com.ssafy.videoconference.model.request.bean.Request;

public class RequestCustomRepositoryImpl implements RequestCustomRepository{
	
	
	@PersistenceContext
	private EntityManager em;
	

	@Override
	public void deleteByNo(int requestNo) {
		String jpql =
				"delete from request where request_no = :requestNo";
		NativeQuery<Request> query = (NativeQuery<Request>) em.createNativeQuery(jpql, Request.class);
		query.setParameter("requestNo", requestNo);
		query.executeUpdate();
	}

	
	@Override
	public List findSendRequest(int groupNo, String hostId) {
		String jpql =
				"select * from request where group_no = :groupNo and host_id = :hostId";
		NativeQuery<Request> query = (NativeQuery<Request>) em.createNativeQuery(jpql, Request.class);
		query.setParameter("groupNo", groupNo);
		query.setParameter("hostId",  hostId);
		return query.getResultList();
	}

	
	@Override
	public List findReceiveRequest(String userId) {
		String jpql =
				"select * from request where user_id = :userId";
		NativeQuery<Request> query = (NativeQuery<Request>) em.createNativeQuery(jpql, Request.class);
		query.setParameter("userId", userId);
		return query.getResultList();
	}

	
	@Override
	public Request findRequest(int groupNo, String hostId, String userId) {
		String jpql =
				"select * from request where group_no = :groupNo and host_id = :hostId and user_id = :userId";
		NativeQuery<Request> query = (NativeQuery<Request>) em.createNativeQuery(jpql, Request.class);
		query.setParameter("groupNo", groupNo);
		query.setParameter("hostId",  hostId);
		query.setParameter("userId", userId);
		return query.getSingleResult();
	}

}
