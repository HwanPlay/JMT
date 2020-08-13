package com.ssafy.videoconference.controller.result;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ssafy.videoconference.model.request.bean.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

public class RequestResult {
	
	
	public static ResponseEntity<ApiResult> build(Request req) {
		ApiResult apiResult = ApiResult.blank()
				.add("requestNo", req.getRequestNo())
				.add("groupNo",  req.getGroup().getGroupNo())
				.add("hostId",  req.getHostId())
				.add("userId", req.getUserId())
				.add("groupName", req.getGroupName());
		return Result.ok(apiResult);
	}
	
	
	public static ResponseEntity<ApiResult> build(List<Request> requests) {
		ArrayList<RequestData> requestsData = new ArrayList<>();
		for(Request request : requests) {
			requestsData.add(new RequestData(request.getRequestNo(),
											request.getGroup().getGroupNo(),
											request.getHostId(),
											request.getUserId(),
											request.getGroupName()));
		}
		
		ApiResult apiResult = ApiResult.blank()
				.add("requests", requestsData);
		return Result.ok(apiResult);
	}
	
	
	@Data
	@AllArgsConstructor
	private static class RequestData {
		private int requestNo;
		private int groupNo;
		private String hostId;
		private String userId;
		private String groupName;
	}
	
	
}
