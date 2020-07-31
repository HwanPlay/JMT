package com.ssafy.videoconference.model.bean;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
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
	private int note_no;
	
	
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
	
	
	
	@OneToOne
	@JoinColumn(name = "meetingNo", nullable = false)
	private Meeting meeting;
	
	
}