package com.hjun.timereport.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectRegisterReq {
	private String projectCode;
	private String projectNm;
	private String startedAt;
	private String endedAt;
	private String projectManager;

	@Builder
	public ProjectRegisterReq(String projectCode, String projectNm, String startedAt, String endedAt,
			String projectManager) {
		this.projectCode = projectCode;
		this.projectNm = projectNm;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.projectManager = projectManager;
	}



}
