package com.ssafy.videoconference.model.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.videoconference.model.user.bean.User;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {
	
	// 아이디랑 패스워드로 유저 찾기
	User findByIdAndPw(String id, String pw);
	

	// Optional : NULL 처리를 돕는 클래스
	// 'null일 수도 있는 객체'를 감싸는 일종의 Wrapper 클래
	// 아이디로 유저 찾기
	Optional<User> findById(String id);
	
	// 이름으로 유저 찾기
	List<User> findByName(String name);
	
	

	@Transactional
	@Modifying
	@Query(value = "update User u set u.pw = :#{#user.pw} WHERE u.id = :#{#user.id}", nativeQuery = false)
	void updateUserPwByUserId(@Param("user") User user);
	
	@Query(value = "select u from User u where u.name like concat('%',:name,'%') and u.id not in (select gm.user.id from GroupMember gm where gm.group.groupNo = :group_no)", nativeQuery = false)
	List<User> listUserByUserIdAndGroupNo(String name, int group_no);
	
}