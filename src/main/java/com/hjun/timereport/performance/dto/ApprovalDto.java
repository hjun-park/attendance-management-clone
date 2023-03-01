package com.hjun.timereport.performance.dto;


import com.hjun.timereport.member.entity.Member;
import com.hjun.timereport.performance.entity.Performance;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalDto {

	private Integer perfDay;
	private String deptNm;
	private Long memberId;
	private String memberNm;
	private String signStatus;
	private Double dayHour;
	private Double totalTime;
	private String isHoliday;
	private String isDeadline;


	@Builder
	public ApprovalDto(
			Integer perfDay,
			String deptNm,
			Long memberId,
			String memberNm,
			String signStatus,
			Double dayHour,
			Double totalTime,
			String isHoliday,
			String isDeadline) {
		this.perfDay = perfDay;
		this.deptNm = deptNm;
		this.memberId = memberId;
		this.memberNm = memberNm;
		this.signStatus = signStatus;
		this.dayHour = dayHour;
		this.totalTime = totalTime;
		this.isHoliday = isHoliday;
		this.isDeadline = isDeadline;
	}

	public static ApprovalDto of(Performance performance, Member member, Double totalTime, String isHoliday, String isDeadline) {
		return ApprovalDto.builder()
				.perfDay(Integer.parseInt(performance.getPerfDay()))
				.deptNm(member.getDeptNm())
				.memberId(member.getMemberId())
				.memberNm(member.getMemberNm())
				.signStatus(performance.getSignStatus())
				.dayHour(performance.getDayHour())
				.totalTime(totalTime)
				.isHoliday(isHoliday)
				.isDeadline(isDeadline)
				.build();
	}

	public static ApprovalDto ofAtHoliday(String perfDay, Member member, Double totalTime, String isHoliday, String isDeadline) {
		return ApprovalDto.builder()
				.perfDay(Integer.parseInt(perfDay))
				.deptNm(member.getDeptNm())
				.memberId(member.getMemberId())
				.memberNm(member.getMemberNm())
				.signStatus("0")
				.dayHour(0.0)
				.totalTime(totalTime)
				.isHoliday(isHoliday)
				.isDeadline(isDeadline)
				.build();
	}

}
