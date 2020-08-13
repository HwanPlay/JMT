package com.ssafy.videoconference.config.oauth;

import java.util.Map;

import com.ssafy.videoconference.model.user.bean.User;
import com.ssafy.videoconference.model.user.bean.UserRole;

import lombok.Data;

@Data
public class OAuthAttributes {
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String picture;


	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
			Map<String, Object> attributes) {
		System.out.println(registrationId);
		if ("naver".equals(registrationId)) {
			return ofNaver("id", attributes);
		} else if ("kakao".equals(registrationId)) {
			return ofKakao("id", attributes);
		} else if ("google".equals(registrationId)) {
			return ofGoogle(userNameAttributeName, attributes);
		}
		return null;
	}

	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		OAuthAttributes oAuthAttributes = new OAuthAttributes();
		oAuthAttributes.setName((String) attributes.get("name"));
		oAuthAttributes.setEmail((String) attributes.get("email"));
		oAuthAttributes.setPicture((String) attributes.get("picture"));
		oAuthAttributes.setAttributes(attributes);
		oAuthAttributes.setNameAttributeKey(userNameAttributeName);
		return oAuthAttributes;
	}

	private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		OAuthAttributes oAuthAttributes = new OAuthAttributes();
		oAuthAttributes.setName((String) response.get("name"));
		oAuthAttributes.setEmail((String) response.get("email"));
		oAuthAttributes.setPicture((String) response.get("profile_image"));
		oAuthAttributes.setAttributes(response);
		oAuthAttributes.setNameAttributeKey(userNameAttributeName);
		return oAuthAttributes;
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		OAuthAttributes oAuthAttributes = new OAuthAttributes();
		oAuthAttributes.setName((String) response.get("name"));
		oAuthAttributes.setEmail((String) response.get("email"));
		oAuthAttributes.setPicture((String) response.get("profile_image"));
		oAuthAttributes.setAttributes(response);
		oAuthAttributes.setNameAttributeKey(userNameAttributeName);
		return oAuthAttributes;
	}

	public User toEntity() {
		User user = new User();
		user.setId(email);
		user.setRole(UserRole.GUEST);
		return user;
	}
}
