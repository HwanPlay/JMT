package com.ssafy.videoconference.model.request.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.videoconference.model.common.BaseTimeEntity;
import com.ssafy.videoconference.model.group.bean.Group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table(name = "request",
uniqueConstraints= {
		@UniqueConstraint(columnNames= {"groupNo", "hostId", "userId"})
})
public class Request extends BaseTimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int requestNo;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupNo", nullable = false)
	private Group group;
	
	
	@Column(name = "hostId", nullable = false)
	private String hostId;
	
	
	@Column(name = "userId", nullable = false)
	private String userId;
	
	
	@Column(name = "groupName", nullable = false)
	private String groupName;
	
	
	public static Request create(int groupNo, String hostId, String userId, String groupName) {
		Request req = new Request();
		req.group = new Group();
		req.getGroup().setGroupNo(groupNo);
		req.hostId = hostId;
		req.userId = userId;
		req.groupName = groupName;
		return req;
	}
	
}