package com.hjun.timereport.task.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "TASK")
@SequenceGenerator(name = "TASK_SEQ_GENERATOR",
				   sequenceName = "TASK_SEQ", initialValue = 1, allocationSize = 1)
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
				generator = "TASK_SEQ_GENERATOR")
	@Column(name = "task_id")
	private Long taskId;

	@Column(name = "group_id")
	private String groupId;

	@Column(name = "code_id")
	private String codeId;

	@Column(name = "code_nm")
	private String codeNm;

	public Task() {
	}

	@Builder
	public Task(String groupId, String codeId, String codeNm) {
		this.groupId = groupId;
		this.codeId = codeId;
		this.codeNm = codeNm;
	}

}
