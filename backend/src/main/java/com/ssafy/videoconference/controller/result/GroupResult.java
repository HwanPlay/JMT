package com.ssafy.videoconference.controller.result;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ssafy.videoconference.model.bean.Group;

import lombok.AllArgsConstructor;
import lombok.Data;

public class GroupResult {
	
	public static ResponseEntity<ApiResult> build(Group group) {
		ApiResult apiResult = ApiResult.blank()
				.add("groupName", group.getGroupName())
				.add("hostId",  group.getHostId());
		return Result.ok(apiResult);
	}
	
	public static ResponseEntity<ApiResult> build(List<Group> groups) {
		ArrayList<GroupData> groupsData = new ArrayList<>();
		for(Group group : groups) {
			groupsData.add(new GroupData(group.getGroupNo(), group.getHostId(), group.getGroupName()));
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
		private String groupName;
	}
}