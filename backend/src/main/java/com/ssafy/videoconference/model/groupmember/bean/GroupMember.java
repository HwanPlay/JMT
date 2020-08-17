package com.ssafy.videoconference.model.groupmember.bean;

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
import com.ssafy.videoconference.model.user.bean.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table(name = "groupMember", 
uniqueConstraints={
	@UniqueConstraint(columnNames={"groupNo","id"})
	})

public class GroupMember extends BaseTimeEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int groupMemberNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupNo", nullable = false)
	private Group group;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id", nullable = false)
	private User user;
	
	@Column(name = "nickname")
	private String nickname;
	
	
	public static GroupMember create(int groupNo, String id, String nickname) {
		GroupMember gm = new GroupMember();
		gm.group = new Group();
		gm.user = new User();
		gm.getGroup().setGroupNo(groupNo);
		gm.getUser().setId(id);
		gm.nickname = nickname;
		return gm;
	}
	
	
	
}