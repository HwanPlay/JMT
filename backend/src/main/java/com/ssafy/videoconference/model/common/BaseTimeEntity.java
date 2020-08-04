package com.ssafy.videoconference.model.common;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseTimeEntity {
	
	
	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime createdDate;
	
	
	@Column(nullable = false, updatable = true)
	@LastModifiedDate
	private LocalDateTime modifiedDate;
	
}
