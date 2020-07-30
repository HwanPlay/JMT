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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Table(name = "note")
public class Note {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int note_no;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_no", nullable = false)
	private Group group;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", nullable = false)
	private User user;
	
	
	@Column(name = "title", nullable = false)
	private String title;
	
	
	@Column(name = "content", nullable = false)
	private String content;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "write_date", nullable = false)
	private Date write_date;
	
	
	@OneToOne
	@JoinColumn(name = "meeting_no", nullable = false)
	private Meeting meeting;
	
	
}