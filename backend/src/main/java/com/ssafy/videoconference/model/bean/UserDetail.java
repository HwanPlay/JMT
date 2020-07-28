package com.ssafy.videoconference.model.bean;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Delegate;


@AllArgsConstructor
@Getter
public class UserDetail implements UserDetails{
	
	//정적 메서드 또는 클래스 인스턴스 및 해당 클래스의 인스턴스 메서드를 참조하는 데이터 구조
	@Delegate
	private User user;
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public String getUsername() {
		return user.getId();
	}
	
	@Override
	public String getPassword() {
		return user.getPw();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}

	

	
	

}
