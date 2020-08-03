package com.ssafy.videoconference.model.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;

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
		@UniqueConstraint(columnNames={"hostId","groupName"})
		})
public class Group extends BaseTimeEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int groupNo;
	
	@Column(name = "hostId", nullable = false)
	private String hostId;

	
	@Column(name = "groupName", nullable = false)
	private String groupName;

	
	@Column(name = "groupIntro")
	private String groupIntro;
	
	
	@Column(name = "hasMeeting")
	private boolean hasmeeting;
	
	
	@OneToMany()
	private List<GroupMember> members = new ArrayList<GroupMember>();
	
	
	@OneToMany()
	private List<Meeting> meetings = new ArrayList<Meeting>();


}