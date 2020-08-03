package com.ssafy.videoconference.model.user.service;

import org.springframework.stereotype.Service;

import com.ssafy.videoconference.model.user.bean.User;
import com.ssafy.videoconference.model.user.repository.JpaUserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl implements IUserService {

	@NonNull
	private JpaUserRepository jpaUserRepo;
	
	@Override
	public User login(User user) {
		return jpaUserRepo.findByIdAndPw(user.getId(), user.getPw());
	}

	@Override
	public User register(User user) {
		return jpaUserRepo.save(user);
	}

	
	@Override
	public User findUserByUserId(String userId) {
		return jpaUserRepo.findById(userId).orElse(null);
	}

	@Override
	public void modifyPw(User user) {
		User modifyUser = new User();
		modifyUser = jpaUserRepo.findById(user.getId()).get();
		modifyUser.setPw(user.getPw());
		System.out.println(user.toString());
		jpaUserRepo.save(modifyUser);
		
//		jpaUserRepo.updateUserPwByUserId(user);
	}
	
	@Override
	public void modifyUser(User user) {
		User preUser = new User();
		preUser = jpaUserRepo.findById(user.getId()).get();
		
		user.setRole(preUser.getRole());
		jpaUserRepo.save(user);
	}

	@Override
	public void removeUser(String userId) {
		jpaUserRepo.deleteUser(userId);
	}
	
	
}