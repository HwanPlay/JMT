package com.ssafy.videoconference.model.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "meeting")
public class Meeting {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int meeting_no;
	
	
	@ManyToOne
	@JoinColumn(name = "group_no", nullable = false)
	private Group group;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "meeting_date", nullable = false)
	private Date meeting_date;
	
	
	@Column(name = "title", nullable = false)
	private String title;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", nullable = false)
	private Date start_time;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", nullable = false)
	private Date end_time;
	
}
