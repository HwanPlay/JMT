package com.ssafy.videoconference.model.note.bean;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.videoconference.model.common.BaseTimeEntity;
import com.ssafy.videoconference.model.group.bean.Group;
import com.ssafy.videoconference.model.meeting.bean.Meeting;
import com.ssafy.videoconference.model.user.bean.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table(name = "note")
public class Note extends BaseTimeEntity{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int noteNo;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupNo", nullable = false)
	private Group group;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", nullable = false)
	private User user;
	
	
	@Column(name = "title", nullable = false)
	private String title;
	
	
	@Column(name = "content", nullable = false)
	private String content;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "meetingNo", nullable = false)
	private Meeting meeting;
	
	
	public static Note create(int groupNo, String id, String title, String content, int meetingNo) {
		Note note = new Note();
		note.group = new Group();
		note.user = new User();
		note.meeting = new Meeting();
		
		note.group.setGroupNo(groupNo);
		note.user.setId(id);
		note.title = title;
		note.content = content;
		note.meeting.setMeetingNo(meetingNo);
		return note;
	}

}