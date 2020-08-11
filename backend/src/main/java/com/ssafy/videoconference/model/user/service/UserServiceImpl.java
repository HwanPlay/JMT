package com.ssafy.videoconference.model.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.videoconference.model.user.bean.FindUser;
import com.ssafy.videoconference.model.user.bean.ModifyUser;
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
		jpaUserRepo.updateUserPwByUserId(user);
	}
	
	@Override
	public void modifyUser(ModifyUser user) {
		System.out.println(user.toString());
		Optional<User> modifyUser = jpaUserRepo.findById(user.getId());
		modifyUser.ifPresent(selectUser->{
			selectUser.setName(user.getName());
			selectUser.setProfile_img(user.getProfile_img());
	//		selectUser.setPw(user.getPw());
			jpaUserRepo.save(selectUser);
		});
	}
	
	@Override
	public void modifyUserProfileImg(User user) {
		Optional<User> modifyUser = jpaUserRepo.findById(user.getId());
		modifyUser.ifPresent(selectUser->{
			selectUser.setProfile_img(user.getProfile_img());
			jpaUserRepo.save(selectUser);
		});
	}

	@Override
	public boolean removeUser(String id) {
		System.out.println(id);
		if(jpaUserRepo.deleteUser(id) != 0)
			return true;
		return false;
		
//		System.out.println(jpaUserRepo.findById(id));
//		Optional<User> user = jpaUserRepo.findById(id);
//		System.out.println("delete : " + user.toString());
//		user.ifPresent(selectUser->{
//			jpaUserRepo.delete(selectUser);
//		});
	}

	@Override
	public List<FindUser> findUserByUserName(String userName, int groupNo, String authUser) {
		
		List<User> userList = jpaUserRepo.listUserByUserIdAndGroupNo(userName, groupNo, authUser);
		List<FindUser> findList = new ArrayList<FindUser>(); 
		for(User user : userList) {
			findList.add(new FindUser(
					user.getId(),
					user.getName(),
					user.getProfile_img()
					));
		}
		return findList;
	}
	
	
}