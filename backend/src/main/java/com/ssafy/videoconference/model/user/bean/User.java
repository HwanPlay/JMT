package com.ssafy.videoconference.model.user.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ssafy.videoconference.model.common.BaseTimeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class User implements Serializable {
   @Id
   @Email(message = "이메일 형식으로 입력해 주세요.")
   @NotBlank(message = "아이디를 입력해 주세요.")
   private String id;

   @NotBlank(message = "이름을 입력해주세요")
   private String name;
   
   @NotBlank(message = "패스워드를 입력해주세요.")
   @Size(max=200)
   private String pw;
   
   @Temporal(TemporalType.TIMESTAMP)
   private Date register_date = new Date();
   
   private String profile_img;
   
   @Enumerated(EnumType.STRING)
   private UserRole role;
   
   
   @Builder
   public User(String id, String pw) {
	   	this.id = id;
	   	this.pw = pw;
   }


   @Builder
public User(String id, String name,String pw, String profile_img) {
	super();
	this.id = id;
	this.name = name;
	this.pw = pw;
	this.profile_img = profile_img;
}
   
   

}