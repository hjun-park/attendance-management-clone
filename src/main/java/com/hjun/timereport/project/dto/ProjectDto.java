package com.hjun.timereport.project.dto;

import com.hjun.timereport.project.entity.Project;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ProjectDto {

	private Long projectId;
	private String projectCode;
	private String projectNm;
	private String startedAt;
	private String endedAt;
	private String projectManager;
	private String isMyProject;

	public static ProjectDto convertToDto (Project project, String isMyProject) {
		return ProjectDto.builder()
				.projectId(project.getProjectId())
				.projectCode(project.getProjectCode())
				.projectNm(project.getProjectNm())
				.startedAt(project.getStartedAt())
				.endedAt(project.getEndedAt())
				.projectManager(project.getProjectManager())
				.isMyProject(isMyProject)
				.build();
	}

	@Builder
	public ProjectDto(Long projectId, String projectCode, String projectNm, String startedAt, String endedAt,
			String projectManager, String isMyProject) {
		this.projectId = projectId;
		this.projectCode = projectCode;
		this.projectNm = projectNm;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.projectManager = projectManager;
		this.isMyProject = isMyProject;
	}

}
