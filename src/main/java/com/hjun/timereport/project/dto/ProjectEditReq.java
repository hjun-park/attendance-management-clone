package com.hjun.timereport.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectEditReq {
	private Long projectId;
	private String projectCode;
	private String projectNm;
	private String startedAt;
	private String endedAt;
	private String projectManager;

	@Builder
	public ProjectEditReq(Long projectId, String projectCode, String projectNm, String startedAt, String endedAt,
			String projectManager) {
		this.projectId = projectId;
		this.projectCode = projectCode;
		this.projectNm = projectNm;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.projectManager = projectManager;
	}
}
