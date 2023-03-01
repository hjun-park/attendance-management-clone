package com.hjun.timereport.deadline.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Entity
@DynamicInsert
@Table(name = "DEADLINE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deadline {

	@Id
	@Column(name = "deadline_day")
	private String deadlineDay;

	@Column(name = "is_deadline")
	private String isDeadline;

	@Builder
	public Deadline(String deadlineDay, String isDeadline) {
		this.deadlineDay = deadlineDay;
		this.isDeadline = isDeadline;
	}

	public void updateIsDeadline(String isDeadline) {
		this.isDeadline = isDeadline;
	}

}
