package com.ssafy.videoconference.config.oauth;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ssafy.videoconference.model.user.bean.User;
import com.ssafy.videoconference.model.user.bean.UserRole;
import com.ssafy.videoconference.model.user.repository.JpaUserRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	@Autowired
	private JpaUserRepository userRepo;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
				.getUserNameAttributeName();

		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
				oAuth2User.getAttributes());

		User user = saveOrUpdate(attributes);

		System.out.println(user.getRoleKey());
		System.out.println(attributes.getAttributes());
		System.out.println(attributes.getNameAttributeKey());
		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
				attributes.getAttributes(), attributes.getNameAttributeKey());
	}

	private User saveOrUpdate(OAuthAttributes attributes) {
		String user_email = attributes.getEmail();
		if (userRepo.findById(user_email).orElse(null) == null) {
			User user = new User();
			user.setId(attributes.getEmail());
			user.setName((attributes.getName()));
			user.setRole(UserRole.GUEST);
			userRepo.save(user);
			return user;
		} else {
			return userRepo.findById(attributes.getEmail()).get();
		}
		
	}

}
