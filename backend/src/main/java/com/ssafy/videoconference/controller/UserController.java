package com.ssafy.videoconference.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

//http://localhost:8080/videoconference/swagger-ui.html
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/api/member")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	
	@ApiOperation(value = "로그인", response = String.class)
	@PostMapping("/test")
	public ResponseEntity<String> login(){
		int a = 1;
		if(a == 1) {
			return ResponseEntity.ok(SUCCESS);
		}else {
			return ResponseEntity.ok(FAIL);
		}
	}
}
