package com.hjun.timereport.project;

import java.util.List;

import com.hjun.timereport.project.dto.ProjectDto;
import com.hjun.timereport.project.dto.ProjectEditReq;
import com.hjun.timereport.project.dto.ProjectRegisterReq;

public interface ProjectService {

	// 프로젝트 전체 조회
	List<ProjectDto> findAll(Long memberId);

	// 내 프로젝트 추가
	Long addMyProject(Long projectId, Long memberId);

	// 내 프로젝트 리스트 조회
	List<ProjectDto> findMyProject(Long memberId);

	Long deleteMyProject(Long projectId, Long memberId);

	// 관리자 프로젝트 추가
	Long addProject(ProjectRegisterReq projectRegisterReq, Long memberId);

	Long editProject(ProjectEditReq projectEditReq, Long memberId);

	Long deleteProject(Long projectId, Long memberId);
}
