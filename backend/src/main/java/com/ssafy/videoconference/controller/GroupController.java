package com.ssafy.videoconference.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.videoconference.controller.command.AddMemberCommand;
import com.ssafy.videoconference.controller.command.ChangeGroupNameCommand;
import com.ssafy.videoconference.controller.command.ChangeHostIdCommand;
import com.ssafy.videoconference.controller.command.ChangeIntroCommand;
import com.ssafy.videoconference.controller.command.CreateGroupCommand;
import com.ssafy.videoconference.controller.payload.ChangeGroupNamePayload;
import com.ssafy.videoconference.controller.payload.ChangeHostIdPayload;
import com.ssafy.videoconference.controller.payload.ChangeIntroPayload;
import com.ssafy.videoconference.controller.payload.CreateGroupPayload;
import com.ssafy.videoconference.controller.result.ApiResult;
import com.ssafy.videoconference.controller.result.GroupResult;
import com.ssafy.videoconference.controller.result.Result;
import com.ssafy.videoconference.model.group.bean.Group;
import com.ssafy.videoconference.model.group.service.GroupService;
import com.ssafy.videoconference.model.groupmember.service.Group_memberService;
import com.ssafy.videoconference.model.meeting.service.MeetingService;
import com.ssafy.videoconference.model.note.service.NoteService;
import com.ssafy.videoconference.model.user.bean.CurrentUser;
import com.ssafy.videoconference.model.user.bean.UserDetail;


@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/api/group")
public class GroupController {

	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private Group_memberService gmService;
	
	@Autowired
	private NoteService noteService;
	
	
	@PostMapping("/add")
	public ResponseEntity<ApiResult> addGroup(@RequestBody CreateGroupPayload payload) {
		CreateGroupCommand command = payload.toCommand();
		Group gp = groupService.createGroup(command);
		return GroupResult.build_add(gp);
	}
	
	
	@GetMapping("/getno/{groupNo}")
	public ResponseEntity<ApiResult> getGroupByNo(@PathVariable("groupNo") int groupNo) {
		Group gp = groupService.findById(groupNo);
		return GroupResult.build(gp);
	}
	
	
	@GetMapping("/gethost/{hostId}")
	public ResponseEntity<ApiResult> getGroupByHost(@PathVariable("hostId") String hostId) {
		List<Group> gp_list = groupService.findByHostId(hostId);
		return GroupResult.build(gp_list);
	}
	
	
	@GetMapping("/get/{userid}")
	public ResponseEntity<ApiResult> getGroup(@PathVariable("userid") String userid) {
		List<Group> gp_list = groupService.findByUserId(userid);
		return GroupResult.build(gp_list);
	}
	
	
	@GetMapping("/get/me")
	public ResponseEntity<ApiResult> getGroupMine(@CurrentUser UserDetail user) {
		List<Group> gp_list_host = groupService.findByHostId(user.getId());
		List<Group> gp_list_member = groupService.findByUserId(user.getId());
		return GroupResult.build(gp_list_host, gp_list_member);
	}

	
	@GetMapping("/get/all/{id}")
	public ResponseEntity<ApiResult> getGroupAll(@PathVariable("id") String id) {
		List<Group> gp_list_host = groupService.findByHostId(id);
		List<Group> gp_list_member = groupService.findByUserId(id);
		return GroupResult.build(gp_list_host, gp_list_member);
	}
	
	

	@DeleteMapping("/delno/{groupNo}")
	public ResponseEntity<ApiResult> deleteByNo(@PathVariable("groupNo") int groupNo) {
		noteService.deleteByGroup(groupNo);
		meetingService.deleteByGroup(groupNo);
		gmService.deleteByGroup(groupNo);
		groupService.deleteByNo(groupNo);
		return Result.ok();
	}
	
	
	@DeleteMapping("/delhost/{hostId}")
	public ResponseEntity<ApiResult> deleteById(@PathVariable("hostId") String hostId) {
		groupService.deleteByHostId(hostId);
		return Result.ok();
	}
	
	
	@DeleteMapping("/delhost")
	public ResponseEntity<ApiResult> deleteMyGroup(@CurrentUser UserDetail user) {
		groupService.deleteByHostId(user.getId());
		return Result.ok();
	}

	
	@PutMapping("/host/{groupNo}")
	public ResponseEntity<ApiResult> changeHostId(@PathVariable("groupNo") int groupNo, 
													@RequestBody ChangeHostIdPayload payload,
														@CurrentUser UserDetail user) {
		ChangeHostIdCommand command = payload.toCommand(groupNo);
		groupService.changeHostId(command);
		gmService.deleteById(groupNo, command.getHostId());
		gmService.addMember(new AddMemberCommand(groupNo, user.getId(), user.getName()));
		return Result.ok();
	}
	
	
	@PutMapping("/name/{groupNo}")
	public ResponseEntity<ApiResult> changeGroupName(@PathVariable("groupNo") int groupNo,
														@RequestBody ChangeGroupNamePayload payload) {
		ChangeGroupNameCommand command = payload.toCommand(groupNo);
		groupService.changeGroupName(command);
		return Result.ok();
	}
	
	
	@PutMapping("/intro/{groupNo}")
	public ResponseEntity<ApiResult> changeIntro(@PathVariable("groupNo") int groupNo,
													@RequestBody ChangeIntroPayload payload) {
		ChangeIntroCommand command = payload.toCommand(groupNo);
		groupService.changeIntro(command);
		return Result.ok();
	}
	
	
	@PutMapping("/hasmeeting/{groupNo}")
	public ResponseEntity<ApiResult> changeHasMeeting(@PathVariable("groupNo") int groupNo) {
		groupService.changeHasMeeting(groupNo);
		return Result.ok();
	}
	
	
}