package com.ssafy.videoconference.controller;

import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.videoconference.config.util.JwtTokenUtil;
import com.ssafy.videoconference.model.user.bean.User;
import com.ssafy.videoconference.model.user.bean.UserRole;
import com.ssafy.videoconference.model.user.service.IUserService;

import io.swagger.annotations.ApiOperation;

//http://localhost:8080/videoconference/swagger-ui.html
@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/api")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Resource(name = "userService")
	private IUserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender emailSender;

	@ApiOperation(value = "로그인 test", response = String.class)
	@PostMapping("/login")
	public ResponseEntity<String> login(){
		
		if(true) {
			return ResponseEntity.ok(SUCCESS);
		}else {
			return ResponseEntity.ok(FAIL);
		}
	}
	
	
	@ApiOperation(value = "패스워드 수정 - modifyUserPwByUserId", response = String.class)
	@PostMapping("/modifyPw")
	public ResponseEntity<String> modifyUserPw(@RequestBody User user) {
		user.setPw(passwordEncoder.encode(user.getPw()));
		userService.modifyPw(user);
		
		System.out.println("modify user : " + user.toString());
		return ResponseEntity.ok(SUCCESS);
	}
	
	@ApiOperation(value = "회원 수정 - modifyUserByUserId", response = String.class)
	@PostMapping("/modifyUser")
	public ResponseEntity<String> modifyUser(@RequestBody  User user) {
		user.setPw(passwordEncoder.encode(user.getPw()));
		userService.modifyUser(user);
		
		System.out.println("modify user : " + user.toString());
		return ResponseEntity.ok(SUCCESS);
	}
	
	
	@ApiOperation(value = "회원가입", response = String.class)
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {
		user.setPw(passwordEncoder.encode(user.getPw()));
		user.setProfile_img("");
		user.setRole(UserRole.USER);
		
		System.out.println(user.toString());

		if(userService.register(user)!=null)
			return ResponseEntity.ok(SUCCESS);
		return ResponseEntity.ok(FAIL);
	}
	
	@ApiOperation(value = "회원탈퇴", response = String.class)
	@DeleteMapping("/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestBody String userId){
		userService.removeUser(userId);
		return ResponseEntity.ok(SUCCESS);
	}
	
	@ApiOperation(value = "아이디 중복 체크", response = String.class)
	@GetMapping("/register/duplicateId/{id}")
	public ResponseEntity<String> duplicateId(@PathVariable("id") String userId){
		
		// 아이디 중복체크 성공 시, 이메일 계정 인증 메일 전송
		if(userService.findUserByUserId(userId)==null) 
			return ResponseEntity.ok(sendEmail(userId));
		else
			return ResponseEntity.ok(FAIL);
	}
	
	@ApiOperation(value = "아이디로 패스워드 찾기", response = String.class)
	@GetMapping("/register/findPw/{id}")
	public ResponseEntity<String> findPw(@PathVariable("id") String userId){
		
		if(userService.findUserByUserId(userId)!=null) 
			return ResponseEntity.ok(SUCCESS);
		else
			return ResponseEntity.ok(FAIL);
	}
	
	public String sendEmail(String userId) {
		SimpleMailMessage message = new SimpleMailMessage();
		//인증 번호 생성기
        StringBuffer authCode =new StringBuffer();
        Random rnd = new Random();
        for(int i=0;i<6;i++)
        {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
            case 0:
                // a-z
                authCode.append((char) ((int) (rnd.nextInt(26)) + 97));
                break;
            case 1:
                // A-Z
                authCode.append((char) ((int) (rnd.nextInt(26)) + 65));
                break;
            case 2:
                // 0-9
                authCode.append((rnd.nextInt(10)));
                break;
            }
        }
		message.setTo(userId);
		message.setSubject("[JMT] 이메일계정 인증 메일입니다.");
		message.setText(new StringBuffer().append("[이메일 인증]\n")
				.append("안녕하세요, JMT입니다.\n")
				.append("아래 인증코드를 입력하시면 이메일계정 인증이 완료됩니다.\n\n")
				.append("인증코드 : " + authCode).toString()
				);
		emailSender.send(message);
		
		return authCode.toString();
	}
	
	
	
}
