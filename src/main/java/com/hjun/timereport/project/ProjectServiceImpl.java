package com.hjun.timereport.project;

import static com.hjun.timereport.project.dto.ProjectDto.convertToDto;
import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjun.timereport.global.exception.BaseException;
import com.hjun.timereport.global.response.BaseResponseStatus;
import com.hjun.timereport.member.MemberService;
import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.performance.PerformanceService;
import com.hjun.timereport.project.dto.ProjectDto;
import com.hjun.timereport.project.dto.ProjectEditReq;
import com.hjun.timereport.project.dto.ProjectRegisterReq;
import com.hjun.timereport.project.entity.MemberProject;
import com.hjun.timereport.project.entity.Project;
import com.hjun.timereport.project.repository.MemberProjectRepository;
import com.hjun.timereport.project.repository.ProjectRepository;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final MemberProjectRepository memberProjectRepository;
	private final MemberService memberService;
	private final PerformanceService performanceService;


	public ProjectServiceImpl(ProjectRepository projectRepository, MemberProjectRepository memberProjectRepository,
			MemberService memberService, PerformanceService performanceService) {
		this.projectRepository = projectRepository;
		this.memberProjectRepository = memberProjectRepository;
		this.memberService = memberService;
		this.performanceService = performanceService;
	}

	@Override
	public List<ProjectDto> findAll(Long memberId) {
		// 1. 프로젝트 전체 조회 (종료일 이후 건은 조회 X)
		LocalDate today = now();
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		List<ProjectDto> result = new ArrayList<>();

		List<Project> projects= projectRepository.findAll().stream()
		.filter(p -> LocalDate.parse(p.getEndedAt(), inputFormatter).isAfter(today))
		.collect(toList());

		Member targetMember = memberService.findMember(memberId);
		List<Long> myProjectIds = memberProjectRepository.findByMember(targetMember).stream()
				.map(mp -> mp.getProject().getProjectId())
				.collect(toList());

		for(Project project : projects) {
			Long projectId = project.getProjectId();
			if(myProjectIds.contains(projectId)) {
				result.add(ProjectDto.convertToDto(project, "1"));
			} else {
				result.add(ProjectDto.convertToDto(project, "0"));
			}
		}

		return result.stream()
				.sorted(Comparator.comparing(ProjectDto::getStartedAt))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public Long addMyProject(Long projectId, Long memberId) {
		// 1. 회원 ID로 프로젝트 바로 추가
		Member targetMember = memberService.findMember(memberId);
		Project targetProject = projectRepository.findById(projectId).orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_EXIST_PROJECT));
		MemberProject memberProjectEntity = MemberProject.builder()
				.member(targetMember)
				.project(targetProject)
				.build();
		if(memberProjectRepository.findByMemberMemberIdAndProjectProjectId(memberProjectEntity.getMember().getMemberId(), memberProjectEntity.getProject().getProjectId()).isPresent()) {
			System.out.println(memberProjectRepository.findByMemberAndProject(targetMember, targetProject).get().toString());
			new BaseException(BaseResponseStatus.DUPLICATE_PROJECT);
		}

		return memberProjectRepository.save(memberProjectEntity).getMemberProjectId();
	}

	@Override
	public List<ProjectDto> findMyProject(Long memberId) {
		// 1. 회원 ID로 프로젝트 조회
		Member memberEntity = memberService.findMember(memberId);

		// 1. 회원 ID로 memberProject 조회
		List<Long> projectIdList = memberProjectRepository.findByMember(memberEntity).stream()
				.map(mp -> mp.getProject().getProjectId())
				.collect(toList());

		List<ProjectDto> projectDtoList = new ArrayList<>();
		for (Long projectId : projectIdList) {
			Project project = projectRepository.findById(projectId).get();
			projectDtoList.add(convertToDto(project, "1"));
		}
		return projectDtoList;
	}


	@Override
	@Transactional
	public Long deleteMyProject(Long projectId, Long memberId) {
		List<ProjectDto> projectDtoList =  findMyProject(memberId).stream()
		.filter(dto -> dto.getProjectId().equals(projectId)).collect(toList());

		List<Project> projectList = new ArrayList<>();
		for (ProjectDto projectDto : projectDtoList) {
			Project proejct = projectRepository.findById(projectDto.getProjectId()).orElseThrow(
					() -> new BaseException(BaseResponseStatus.NOT_EXIST_PROJECT)
					);
			projectList.add(proejct);
		}

		projectList.stream()
			.filter(p -> !performanceService.findPerformanceWithProject(p.getProjectCode()))
			.forEach(p -> {
				memberProjectRepository.findByProject(p)
					.forEach(mp -> memberProjectRepository.delete(mp));
			});

		return memberId;
	}

	@Override
	@Transactional
	public Long addProject(ProjectRegisterReq projectRegisterReq, Long memberId) {
		Project project = Project.builder()
				.projectCode(projectRegisterReq.getProjectCode())
				.projectNm(projectRegisterReq.getProjectNm())
				.startedAt(projectRegisterReq.getStartedAt())
				.endedAt(projectRegisterReq.getEndedAt())
				.projetManager(projectRegisterReq.getProjectManager())
				.build();

		projectRepository.save(project);
		return memberId;
	}

	@Override
	@Transactional
	public Long editProject(ProjectEditReq projectEditReq, Long memberId) {
		Project project = projectRepository.findById(projectEditReq.getProjectId()).orElseThrow(
				() -> new BaseException(BaseResponseStatus.NOT_EXIST_PROJECT)
				);
		project.editProject(projectEditReq);
		return null;
	}

	@Override
	@Transactional
	public Long deleteProject(Long projectId, Long memberId) {
		Project project = projectRepository.findById(projectId).orElseThrow(
				() -> new BaseException(BaseResponseStatus.NOT_EXIST_PROJECT)
				);

		if(!memberProjectRepository.findByProject(project).isEmpty()) {
			throw new BaseException(BaseResponseStatus.EXIST_PROJECT_WITH_MEMBER);
		}

		if(performanceService.findPerformanceWithProject(project.getProjectCode())) {
			throw new BaseException(BaseResponseStatus.EXIST_PROJECT_WITH_MEMBER);
		}
		projectRepository.delete(project);

		return memberId;
	}

}
