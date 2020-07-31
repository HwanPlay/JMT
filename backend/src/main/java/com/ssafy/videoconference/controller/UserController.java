package com.ssafy.videoconference.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.videoconference.config.util.JwtTokenUtil;
import com.ssafy.videoconference.model.user.bean.User;
import com.ssafy.videoconference.model.user.bean.UserRole;
import com.ssafy.videoconference.model.user.service.IUserService;

import io.swagger.annotations.ApiOperation;
import lombok.NonNull;

//http://localhost:8080/videoconference/swagger-ui.html
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/api")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@ApiOperation(value = "로그인", response = String.class)
	@PostMapping("/login")
	public ResponseEntity<String> login(){
		
		int a = 1;
		if(a == 1) {
			return ResponseEntity.ok(SUCCESS);
		}else {
			return ResponseEntity.ok(FAIL);
		}
	}
	@Resource(name = "userService")
	private IUserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping(value = "/init")
	public String createAdmin() {
		User user = new User();
		System.out.println("user");
		user.setId("admin@naver.com");
		System.out.println("null 이라구?" + passwordEncoder.encode("test") + ";;;;");
		user.setPw(passwordEncoder.encode("test"));
		user.setName("admin");
		user.setRole(UserRole.ADMIN);
		user.setProfile_img("");
		System.out.println(user.getId());
		System.out.println(user.toString());
		
		if (userService.register(user) == null) {
			System.out.println("Create Admin Error");
		}
		return "success";
	}
}
