package com.ssafy.videoconference.model.user.service;

import com.ssafy.videoconference.model.user.bean.User;

public interface IUserService {

	User login(User user);

	User register(User user);

	User findUserByUserId(String userId);

}