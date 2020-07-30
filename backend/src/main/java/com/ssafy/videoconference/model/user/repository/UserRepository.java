package com.ssafy.videoconference.model.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.videoconference.model.user.bean.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByIdAndPw(String id, String pw);
	
	// Optional : NULL 처리를 돕는 클래스
	// 'null일 수도 있는 객체'를 감싸는 일종의 Wrapper 클래스
	Optional<User> findById(String id);

}