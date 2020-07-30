package com.ssafy.videoconference.model.bean;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table(name = "conference",
uniqueConstraints={
		@UniqueConstraint(columnNames={"hostId","groupName"})
		})
public class Group extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int groupNo;
	
	
	@Column(name = "hostId", nullable = false)
	@LastModifiedBy
	private String hostId;
	
	
	@Column(name = "groupName", nullable = false)
	private String groupName;

}
