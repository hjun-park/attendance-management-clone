package com.hjun.timereport.project.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hjun.timereport.project.dto.ProjectEditReq;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "PROJECT")
@SequenceGenerator(name = "PROJECT_SEQ_GENERATOR", sequenceName = "PROJECT_SEQ", initialValue = 1, allocationSize = 1)
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
				generator = "PROJECT_SEQ_GENERATOR")
	@Column(name = "project_id")
	private Long projectId;

	@Column(name = "project_code")
	private String projectCode;

	@Column(name = "project_nm")
	private String projectNm;

	@Column(name = "started_at")
	private String startedAt;

	@Column(name = "ended_at")
	private String endedAt;

	@Column(name = "project_manager")
	private String projectManager;

	@OneToMany(mappedBy = "project", targetEntity = MemberProject.class)
	private List<MemberProject> memberProjects = new ArrayList<>();

	// 외래키 없는 쪽, mappedBy 연관관계 주인이 아닌 쪽에서는 편의 메소드 만들기
	public void addMemberProject(MemberProject memberProject) {
		memberProjects.add(memberProject);
		memberProject.setProject(this);
	}

	public Project() {

	}

	@Builder
	public Project(Long projectId, String projectCode, String projectNm, String startedAt, String endedAt,
			String projetManager, List<MemberProject> memberProjects) {
		this.projectId = projectId;
		this.projectCode = projectCode;
		this.projectNm = projectNm;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.projectManager = projetManager;
		this.memberProjects = memberProjects;
	}

	public void editProject(ProjectEditReq prj) {
		this.projectCode = prj.getProjectCode();
		this.projectNm = prj.getProjectNm();
		this.startedAt = prj.getStartedAt();
		this.endedAt = prj.getEndedAt();
		this.projectManager = prj.getProjectManager();
	}

}
