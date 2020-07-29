package com.ssafy.videoconference.model.bean;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class User{
	@Id
	@NotBlank(message = "")
	@Size(min=5, max=20, message = "")
	private String id;
	
	@NotBlank(message = "")
	private String pw;
	
	@NotBlank(message = "")
	private String name;
	
	@Email
	@NotBlank(message = "")
	private String email;
	
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	private String profile_img;
}

