package com.ssafy.videoconference.model.user.service;

import org.springframework.stereotype.Service;

import com.ssafy.videoconference.model.user.bean.User;
import com.ssafy.videoconference.model.user.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl implements IUserService {

	@NonNull
	private UserRepository userRepository;

	@Override
	public User login(User user) {
		return userRepository.findByIdAndPw(user.getId(), user.getPw());
	}

	@Override
	public User register(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findUserByUserId(String userid) {
		return userRepository.findById(userid).get();
	}

}