package com.hjun.timereport.member.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hjun.timereport.global.config.GradeConverter;
import com.hjun.timereport.member.dto.MemberDto;
import com.hjun.timereport.project.entity.MemberProject;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "MEMBER")
@SequenceGenerator(
		name = "MEMBER_SEQ_GENERATOR",
		sequenceName = "MEMBER_SEQ",
		initialValue = 1, allocationSize = 1)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
				generator = "MEMBER_SEQ_GENERATOR")
	@Column(name = "member_id")
	private Long memberId;

	@Column(name = "member_code")
	private String memberCode;

	@Column(name = "pwd")
	private String pwd;

	@Column(name = "member_nm")
	private String memberNm;

	@Column(name = "grade")
	@Convert(converter = GradeConverter.class)
	private String grade;

	@Column(name = "dept_nm")
	private String deptNm;

	@Column(name = "dept_code")
	private String deptCode;

	@Column(name = "phone_number")
	private String phoneNumber;

	@OneToMany(mappedBy = "member", targetEntity = MemberProject.class)
	private List<MemberProject> memberProjects = new ArrayList<>();

	// 외래키 없는 쪽, mappedBy 연관관계 주인이 아닌 쪽에서는 편의 메소드 만들기
	public void addMemberProject(MemberProject memberProject) {
		memberProjects.add(memberProject);
		memberProject.setMember(this);
	}


	public Member() {
	}

	@Builder
	public Member(String memberCode, String pwd, String memberNm, String grade, String deptNm, String deptCode,
			String phoneNumber) {
		this.memberCode = memberCode;
		this.pwd = pwd;
		this.memberNm = memberNm;
		this.grade = grade;
		this.deptNm = deptNm;
		this.deptCode = deptCode;
		this.phoneNumber = phoneNumber;
	}

	public void editMember(String pwd, String phoneNumber) {
		this.pwd = pwd == null ? this.pwd : pwd;
		this.phoneNumber = phoneNumber == null ? this.phoneNumber : phoneNumber;
	}

	public static MemberDto entityToDto(Member m) {
		return MemberDto.builder()
				.memberId(m.getMemberId())
				.memberCode(m.getMemberCode())
				.pwd(m.getPwd())
				.memberNm(m.getMemberNm())
				.grade(m.getGrade())
				.deptNm(m.getDeptNm())
				.deptCode(m.getDeptCode())
				.phoneNumber(m.getPhoneNumber())
				.build();
	}



}
