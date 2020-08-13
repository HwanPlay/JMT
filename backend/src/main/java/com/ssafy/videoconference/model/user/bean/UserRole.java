package com.ssafy.videoconference.model.user.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum UserRole {
	USER("ROLE_USER", "user"), 
	ADMIN("ROLE_ADMIN", "admin"),
	GUEST("ROLE_GUEST", "guest");
	
	private String value;
	
	private final String key;
	private final String title;
	
	private UserRole(String key, String title) {
		this.key = key;
		this.title = title;
	}
	public String getKey() {
		return key;
	}
	public String getTitle() {
		return title;
	}
	
}
