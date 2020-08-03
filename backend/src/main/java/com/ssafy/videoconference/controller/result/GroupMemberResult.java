package com.ssafy.videoconference.controller.result;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ssafy.videoconference.model.groupmember.bean.GroupMember;

import lombok.AllArgsConstructor;
import lombok.Data;

public class GroupMemberResult {
	
	
	public static ResponseEntity<ApiResult> build(GroupMember gm) {
		ApiResult apiResult = ApiResult.blank()
				.add("groupNo", gm.getGroup().getGroupNo())
				.add("id", gm.getUser().getId())
				.add("nickname", gm.getNickname());
		return Result.ok(apiResult);
	}
	
	
	public static ResponseEntity<ApiResult> build(List<GroupMember> gms) {
		ArrayList<GroupMemberData> gmsData = new ArrayList<>();
		for(GroupMember gm : gms) {
			gmsData.add(new GroupMemberData(gm.getGroupMemberNo(),
											gm.getGroup().getGroupNo(),
											gm.getUser().getId(),
											gm.getNickname()));
		}
		ApiResult apiResult = ApiResult.blank()
				.add("groupMembers", gmsData);
		return Result.ok(apiResult);
	}
	
	
	
	@Data
	@AllArgsConstructor
	private static class GroupMemberData {
		private int groupMemberNo;
		private int groupNo;
		private String id;
		private String nickname;
	}
}
