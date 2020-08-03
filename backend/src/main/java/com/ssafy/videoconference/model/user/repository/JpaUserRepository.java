package com.ssafy.videoconference.model.user.repository;

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

	User findByIdAndPw(String id, String pw);

	// Optional : NULL 처리를 돕는 클래스
	// 'null일 수도 있는 객체'를 감싸는 일종의 Wrapper 클래스
	Optional<User> findById(String id);

	@Transactional
	@Modifying
	@Query(value = "update User u set u.pw = :#{#user.pw} WHERE u.id = :#{#user.id}", nativeQuery = false)
	void updateUserPwByUserId(@Param("user") User user);

	@Transactional
	@Modifying
	@Query("DELETE FROM User u WHERE u.id=:id")
	void deleteUser(@Param("id") String userId);

}