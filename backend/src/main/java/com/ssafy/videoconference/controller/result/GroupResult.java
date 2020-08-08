package com.ssafy.videoconference.controller.result;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ssafy.videoconference.model.group.bean.Group;

import lombok.AllArgsConstructor;
import lombok.Data;

public class GroupResult {
	
	public static ResponseEntity<ApiResult> build(Group group) {
		ApiResult apiResult = ApiResult.blank()
				.add("groupNo", group.getGroupNo())
				.add("hostId",  group.getUser().getId())
				.add("hostName", group.getUser().getName())
				.add("groupName", group.getGroupName())
				.add("groupIntro",  group.getGroupIntro())
				.add("hasMeeting", group.isHasmeeting());
		return Result.ok(apiResult);
	}
	
	public static ResponseEntity<ApiResult> build_add(Group group) {
		ApiResult apiResult = ApiResult.blank()
				.add("groupNo", group.getGroupNo())
				.add("hostId",  group.getUser().getId())
				.add("groupName", group.getGroupName())
				.add("groupIntro",  group.getGroupIntro())
				.add("hasMeeting", group.isHasmeeting());
		return Result.ok(apiResult);
	}
	
	public static ResponseEntity<ApiResult> build(List<Group> group_host, List<Group> group_member) {
		ArrayList<GroupData> groupsData_host = new ArrayList<>();
		ArrayList<GroupData> groupsData_member = new ArrayList<>();
		for(Group group : group_host) {
			groupsData_host.add(new GroupData(group.getGroupNo(),
										group.getUser().getId(),
										group.getUser().getName(),
										group.getGroupName(),
										group.getGroupIntro(),
										group.isHasmeeting()));
		}
		
		for(Group group : group_member) {
			groupsData_member.add(new GroupData(group.getGroupNo(),
										group.getUser().getId(),
										group.getUser().getName(),
										group.getGroupName(),
										group.getGroupIntro(),
										group.isHasmeeting()));
		}
		
		ApiResult apiResult = ApiResult.blank()
				.add("groups_host", groupsData_host)
				.add("groups_member", groupsData_member);
		return Result.ok(apiResult);
	}
	
	
	
	public static ResponseEntity<ApiResult> build(List<Group> groups) {
		ArrayList<GroupData> groupsData = new ArrayList<>();
		for(Group group : groups) {
			groupsData.add(new GroupData(group.getGroupNo(),
										group.getUser().getId(),
										group.getUser().getName(),
										group.getGroupName(),
										group.getGroupIntro(),
										group.isHasmeeting()));
		}
		
		ApiResult apiResult = ApiResult.blank()
				.add("groups", groupsData);
		return Result.ok(apiResult);
	}
	
	@Data
	@AllArgsConstructor
	private static class GroupData {
		private int groupNo;
		private String hostId;
		private String hostName;
		private String groupName;
		private String groupIntro;
		private boolean hasMeeting;
	}
}