package com.hjun.timereport.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hjun.timereport.member.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "MEMBER_PROJECT")
@SequenceGenerator(name = "MEMBER_PROJECT_SEQ_GENERATOR", sequenceName = "MEMBER_PROJECT_SEQ", initialValue = 1, allocationSize = 1)
public class MemberProject {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_PROJECT_SEQ_GENERATOR")
	@Column(name = "member_project_id")
	private Long memberProjectId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;

	public void setProject(Project project) {
		this.project = project;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public MemberProject() {
	}

	@Builder
	public MemberProject(Long memberProjectId, Member member, Project project) {
		this.memberProjectId = memberProjectId;
		this.member = member;
		this.project = project;
	}

}
