package com.ssafy.videoconference.model.user.service;

import java.util.List;

import com.ssafy.videoconference.model.user.bean.FindUser;
import com.ssafy.videoconference.model.user.bean.ModifyUser;
import com.ssafy.videoconference.model.user.bean.User;

public interface IUserService {

	User login(User user);

	User register(User user);
	
	User findUserByUserId(String userId);
	
	List<FindUser> findUserByUserName(String userName, int groupNo, String authUser);
	
	void modifyPw(User user);
	void modifyUser(ModifyUser mUser);
	void modifyUserProfileImg(User user);
	
	boolean removeUser(String id);
	
	
}