package com.ssafy.videoconference.model.group.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.videoconference.model.common.BaseTimeEntity;
import com.ssafy.videoconference.model.groupmember.bean.GroupMember;
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
@Table(name = "meeting_group",
uniqueConstraints={
		@UniqueConstraint(columnNames={"id","groupName"})
		})
public class Group extends BaseTimeEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int groupNo;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", nullable = false)
	private User user;
	
	
	@Column(name = "groupName", nullable = false)
	private String groupName;

	
	@Column(name = "groupIntro")
	private String groupIntro;
	
	
	@Column(name = "hasMeeting")
	private boolean hasmeeting;
	
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<GroupMember> members = new ArrayList<GroupMember>();
	
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<Meeting> meetings = new ArrayList<Meeting>();

	
	public static Group create(String id, String groupName, String groupIntro) {
		Group group = new Group();
		group.user = new User();
		group.user.setId(id);
		group.groupName = groupName;
		group.groupIntro = groupIntro;
		group.hasmeeting = false;
		return group;
	}

}