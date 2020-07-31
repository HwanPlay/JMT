package com.ssafy.videoconference.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.videoconference.controller.result.ApiResult;
import com.ssafy.videoconference.controller.result.GroupResult;
import com.ssafy.videoconference.controller.result.Result;
import com.ssafy.videoconference.model.bean.Group;
import com.ssafy.videoconference.model.service.GroupService;

@RestController
@RequestMapping("/api/group")
public class GroupController {

	
	@Autowired
	private GroupService groupService;
	
	
	@PostMapping("/add")
	public ResponseEntity<ApiResult> addGroup(@RequestBody Group group) {
		Group gp = groupService.createGroup(group);
		return GroupResult.build(gp);
	}
	
	
	@GetMapping("/getno/{groupNo}")
	public ResponseEntity<ApiResult> getGroupByNo(@PathVariable("groupNo") int groupNo) {
		Group gp = groupService.findById(groupNo);
		return GroupResult.build(gp);
	}
	
	
	@GetMapping("/gethost/{hostId}")
	public ResponseEntity<ApiResult> getGroupByhost(@PathVariable("hostId") String hostId) {
		List<Group> gp_list = groupService.findByHostId(hostId);
		return GroupResult.build(gp_list);
	}
	

	@DeleteMapping("/delno/{groupNo}")
	public ResponseEntity<ApiResult> deleteByNo(@PathVariable("groupNo") int groupNo) {
		groupService.deleteByNo(groupNo);
		return Result.ok();
	}
	
	
	@DeleteMapping("/delhost/{hostId}")
	public ResponseEntity<ApiResult> deleteByNo(@PathVariable("hostId") String hostId) {
		groupService.deleteByHostId(hostId);
		return Result.ok();
	}

	
	@PutMapping("/set")
	public ResponseEntity<ApiResult> setGroup(@RequestBody Group group) {
		Group gp = groupService.update(group);
		return Result.ok();
	}
	
	
}
