package com.ssafy.videoconference.controller;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.videoconference.config.util.JwtTokenUtil;
import com.ssafy.videoconference.model.group.service.GroupService;
import com.ssafy.videoconference.model.user.bean.FindUser;
import com.ssafy.videoconference.model.user.bean.User;
import com.ssafy.videoconference.model.user.bean.UserRole;
import com.ssafy.videoconference.model.user.service.IFileService;
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

	private static final String IMGFOLDER = "resources/upload/profile/img";
	private static final String DEFAULT_IMG = "default.jpg";

	@Resource(name = "userService")
	private IUserService userService;

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	ServletContext servletContext;

	@Autowired
	IFileService fileService;

	@ApiOperation(value = "패스워드 수정 - modifyUserPwByUserId", response = String.class)
	@PostMapping("/user/modifyPw")
	public ResponseEntity<String> modifyUserPw(@RequestBody User user) {
		user.setPw(passwordEncoder.encode(user.getPw()));
		userService.modifyPw(user);

		System.out.println("modify user : " + user.toString());
		return ResponseEntity.ok(SUCCESS);
	}

	@ApiOperation(value = "회원 찾기(아이디,이름,프로필사진) - findUserByUserName / 이미 그룹에 속한 사람은 제외", response = List.class)
	@GetMapping("/user/findUserByName")
	public ResponseEntity<List<FindUser>> findUserByUserName(@RequestParam String name, @RequestParam int group_no) {
		List<FindUser> userList = userService.findUserByUserName(name, group_no);
		return ResponseEntity.ok(userList);
	}

	
//	@ApiOperation(value = "회원 찾기(아이디,이름,프로필사진) - findUserByUserName / 이미 그룹에 속한 사람은 제외", response = List.class)
//	@GetMapping("/user/findUserByName/{name}")
//	public ResponseEntity<List<FindUser>> findUserByUserName(@PathVariable String name) {
//		List<FindUser> userList = userService.findUserByUserName(name);
//		
//		return ResponseEntity.ok(userList);
//	}

	
	@ApiOperation(value = "회원 찾기 - findUserByUserId / 내 정보", response = String.class)
	@GetMapping("/user/findUserById/{id}")
	public ResponseEntity<User> findUserByUserId(@PathVariable("id") String userId) {
		return ResponseEntity.ok(userService.findUserByUserId(userId));
	}

	@ApiOperation(value = "회원 수정 - modifyUserByUserId", response = String.class)
	@PostMapping("/user/modify")
	public ResponseEntity<String> modifyUser(@RequestBody User user) {
		user.setPw(passwordEncoder.encode(user.getPw()));
		userService.modifyUser(user);

		System.out.println("modify user : " + user.toString());
		return ResponseEntity.ok(SUCCESS);
	}

	@ApiOperation(value = "회원가입", response = String.class)
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {
		user.setPw(passwordEncoder.encode(user.getPw()));
		user.setProfile_img(DEFAULT_IMG);
		user.setRole(UserRole.USER);

		System.out.println(user.toString());

		if (userService.register(user) != null)
			return ResponseEntity.ok(SUCCESS);
		return ResponseEntity.ok(FAIL);
	}

	@ApiOperation(value = "프로필사진 추가", response = String.class)
	@PostMapping("/user/profileImg")
	public ResponseEntity<String> saveProfileImg(@RequestParam("filename") MultipartFile multipartFile,
			@RequestParam("id") String userId) {
		// MultipartFile : 사용자 PC의 업로드된 스트림정보를 저장
		if (multipartFile != null && !multipartFile.isEmpty()) {

			// 사용자 DB에 저장된 프로필 사진
			String userFileName = userService.findUserByUserId(userId).getProfile_img();
			String saveFileName = userFileName;

			// 파일형
			String fileExtension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

			// 서버 폴더 경로명
			// 파일은 http방식으로 저장되는 것이 아니라, 서버의 하드디스크 전체 경로에 맞추어서 저장
			String realPath = servletContext.getRealPath(IMGFOLDER);

			// 디폴트 프로필이 아니라면, 서버에 올라온 프로필 삭제
			if (!userFileName.contains("default")) {
				File deleteFolder = new File(realPath);
				File[] deleteFolderList = deleteFolder.listFiles();

				for (File file : deleteFolderList) {
					if (file.getPath().contains(userFileName))
						file.delete();
				}
			}

			// 프로필 사진 추가명 : 날짜+랜덤UUID
			DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
			saveFileName = dateFormat.format(new Date()) + '_'
					+ UUID.randomUUID().toString().replace("-", "").substring(0, 10) + '.' + fileExtension;

			// 파일 저장
			fileService.saveFile(multipartFile, realPath, saveFileName);

			// 파일 업로드 후, 사용자 DB에 이미지명 저장
			User user = new User();
			user.setId(userId);
			user.setProfile_img(saveFileName);
			userService.modifyUserProfileImg(user);

		}
		return ResponseEntity.ok(SUCCESS);
	}

	@ApiOperation(value = "프로필사진 삭제 - 디폴트사진으로", response = String.class)
	@GetMapping("/user/delProfileImg/{id}")
	public ResponseEntity<String> saveProfileImg(@PathVariable("id") String userId) {
		// 사용자 DB에 저장된 프로필 사진
		String userFileName = userService.findUserByUserId(userId).getProfile_img();
		String realPath = servletContext.getRealPath(IMGFOLDER);

		// 디폴트 프로필이 아니라면, 서버에 올라온 프로필 삭제
		if (!userFileName.contains("default")) {
			File deleteFolder = new File(realPath);
			File[] deleteFolderList = deleteFolder.listFiles();

			for (File file : deleteFolderList) {
				if (file.getPath().contains(userFileName))
					file.delete();
			}
		}
	
		// 파일 업로드 후, 사용자 DB에 이미지명 저장
		User user = new User();
		user.setId(userId);
		user.setProfile_img(DEFAULT_IMG);
		userService.modifyUserProfileImg(user);
		return ResponseEntity.ok(SUCCESS);
	}

	@ApiOperation(value = "회원탈퇴", response = String.class)
	@DeleteMapping("/user/delUser")
	public ResponseEntity<String> deleteUser(@RequestBody String userId) {
		userService.removeUser(userId);
		return ResponseEntity.ok(SUCCESS);
	}

	@ApiOperation(value = "아이디 중복 체크", response = String.class)
	@GetMapping("/register/duplicateId/{id}")
	public ResponseEntity<String> duplicateId(@PathVariable("id") String userId) {

		// 아이디 중복체크 성공 시, 이메일 계정 인증 메일 전송
		if (userService.findUserByUserId(userId) == null)
			return ResponseEntity.ok(sendEmail(userId));
		else
			return ResponseEntity.ok(FAIL);
	}

	@ApiOperation(value = "아이디로 패스워드 찾기", response = String.class)
	@GetMapping("/register/findPw/{id}")
	public ResponseEntity<String> findPw(@PathVariable("id") String userId) {

		if (userService.findUserByUserId(userId) != null)
			return ResponseEntity.ok(SUCCESS);
		else
			return ResponseEntity.ok(FAIL);
	}

	public String sendEmail(String userId) {
		SimpleMailMessage message = new SimpleMailMessage();
		// 인증 번호 생성기
		StringBuffer authCode = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 6; i++) {
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
		message.setText(new StringBuffer().append("[이메일 인증]\n").append("안녕하세요, JMT입니다.\n")
				.append("아래 인증코드를 입력하시면 이메일계정 인증이 완료됩니다.\n\n").append("인증코드 : " + authCode).toString());
		emailSender.send(message);

		return authCode.toString();
	}

}
