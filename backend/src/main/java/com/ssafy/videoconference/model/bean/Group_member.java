package com.ssafy.videoconference.model.bean;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table(name = "Group_member", 
uniqueConstraints={
	@UniqueConstraint(columnNames={"group_no","id"})
	})

public class Group_member {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Group_member_no;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_no", nullable = false)
	private Group group;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", nullable = false)
	private User user;
	
	
}