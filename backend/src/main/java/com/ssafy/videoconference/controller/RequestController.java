package com.ssafy.videoconference.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.videoconference.controller.command.SendRequestCommand;
import com.ssafy.videoconference.controller.payload.SendRequestPayload;
import com.ssafy.videoconference.controller.result.ApiResult;
import com.ssafy.videoconference.controller.result.RequestResult;
import com.ssafy.videoconference.controller.result.Result;
import com.ssafy.videoconference.model.request.bean.Request;
import com.ssafy.videoconference.model.request.service.RequestService;


@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/api/request")
public class RequestController {
	
	
	@Autowired
	private RequestService reqService;
	
	
	@PostMapping("/send")
	public ResponseEntity<ApiResult> sendRequest(@RequestBody SendRequestPayload payload) {
		SendRequestCommand command = payload.toCommand();
		Request req = reqService.sendRequest(command);
		return RequestResult.build(req);
	}
	
	
	@DeleteMapping("/delno/{requestNo}")
	public ResponseEntity<ApiResult> deleteByNo(@PathVariable("requestNo") int requestNo) {
		reqService.deleteByNo(requestNo);
		return Result.ok();
	}
	
	
	@GetMapping("/get/{groupNo}/{hostId}")
	public ResponseEntity<ApiResult> getRequestByHost(@PathVariable("groupNo") int groupNo,
														@PathVariable("hostId") String hostId) {
		List<Request> req_list = reqService.findSendRequest(groupNo, hostId);
		return RequestResult.build(req_list);
	}
														
														
	@GetMapping("/getuser/{userId}")
	public ResponseEntity<ApiResult> getRequestByUser(@PathVariable("userId") String userId) {
		List<Request> req_list = reqService.findReceiveRequest(userId);
		return RequestResult.build(req_list);
	}
	
	
	@GetMapping("/getReq/{groupNo}/{hostId}/{userId}")
	public ResponseEntity<ApiResult> getRequest(@PathVariable("groupNo") int groupNo,
												@PathVariable("hostId") String hostId,
												@PathVariable("userId") String userId) {
		Request req = reqService.findRequest(groupNo, hostId, userId);
		return RequestResult.build(req);
	}
	

}
