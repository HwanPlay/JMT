package com.ssafy.videoconference.model.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "`group`")
public class Group {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int group_no;
	
	
	@Column(name = "host_id", nullable = false)
	private String host_id;
	
	
	@Column(name = "group_name", nullable = false)
	private String group_name;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="created_date", nullable = false)
	private Date createdDate;
}
