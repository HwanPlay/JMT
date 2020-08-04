package com.ssafy.videoconference.model.meeting.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "meeting")
public class Meeting extends BaseTimeEntity{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int meetingNo;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupNo", nullable = false)
	private Group group;
	
	
	@Column(name = "title", nullable = false)
	private String title;
	
	
	public static Meeting create(int groupNo, String title) {
		Meeting meeting = new Meeting();
		meeting.group = new Group();
		meeting.group.setGroupNo(groupNo);
		meeting.title = title;
		return meeting;
	}
	
	
}
