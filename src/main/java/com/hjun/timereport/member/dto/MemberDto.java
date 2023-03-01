package com.hjun.timereport.member.dto;

import com.hjun.timereport.member.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

	private Long memberId;

	private String memberCode;

	private String pwd;

	private String memberNm;

	private String grade;

	private String deptNm;

	private String deptCode;

	private String phoneNumber;

	@Builder
	public MemberDto(Long memberId, String memberCode, String pwd, String memberNm, String grade, String deptNm,
		String deptCode, String phoneNumber) {
		this.memberId = memberId;
		this.memberCode = memberCode;
		this.pwd = pwd;
		this.memberNm = memberNm;
		this.grade = grade;
		this.deptNm = deptNm;
		this.deptCode = deptCode;
		this.phoneNumber = phoneNumber;
	}

	public static Member dtoToEntity(MemberDto m) {
		return Member.builder()
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
