package com.ssafy.videoconference.model.user.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.videoconference.model.user.bean.UserDetail;
import com.ssafy.videoconference.model.user.repository.JpaUserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	private JpaUserRepository userRepository;

	@Override
	public UserDetail loadUserByUsername(String userId) {
		return userRepository.findById(userId)
				.map(u -> new UserDetail(u, Collections.singleton(new SimpleGrantedAuthority(u.getRole().getValue()))))
				.orElseThrow(() -> new UsernameNotFoundException("User is not exist!"));
	}
}
