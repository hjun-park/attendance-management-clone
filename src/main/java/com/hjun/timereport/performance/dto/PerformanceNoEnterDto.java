package com.hjun.timereport.performance.dto;

import com.hjun.timereport.member.dto.MemberDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceNoEnterDto {

	private Long memberId;
	private String memberNm;
	private String deptCode;
	private String deptNm;
	private String phoneNumber;
	private String noEnterDays;

	public static PerformanceNoEnterDto converToDto (MemberDto memberDto, String noEnterDays) {
		return PerformanceNoEnterDto.builder()
				.memberId(memberDto.getMemberId())
				.memberNm(memberDto.getMemberNm())
				.deptCode(memberDto.getDeptCode())
				.deptNm(memberDto.getDeptNm())
				.phoneNumber(memberDto.getPhoneNumber())
				.noEnterDays(noEnterDays)
				.build();
	}

	@Builder
	public PerformanceNoEnterDto(Long memberId, String memberNm, String deptCode, String deptNm, String phoneNumber,
			String noEnterDays) {
		this.memberId = memberId;
		this.memberNm = memberNm;
		this.deptCode = deptCode;
		this.deptNm = deptNm;
		this.phoneNumber = phoneNumber;
		this.noEnterDays = noEnterDays;
	}


}
