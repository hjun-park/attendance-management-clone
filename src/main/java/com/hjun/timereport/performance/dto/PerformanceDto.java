package com.hjun.timereport.performance.dto;

import com.hjun.timereport.performance.entity.Performance;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceDto {

	private Long perfId;
	private Integer seq;
	private Double taskHour;
	private Integer perfDay;
	private Double dayHour;
	private String startedHour;
	private String endedHour;
	private String groupMainId;
	private String groupSubId;
	private String codeId;
	private String codeMainNm;
	private String codeSubNm;
	private String workDetail;

	private String signStatus;
	private Double breakTime;
	private String overtimeDetail;
	private String wfhYn;
	private String isHoliday;
	private Long memberId;
	private String isDeadline;

	@Builder
	@QueryProjection
	public PerformanceDto(Long perfId, Integer seq, Double taskHour, Integer perfDay, Double dayHour,
			String startedHour, String endedHour, String groupMainId, String groupSubId, String codeId, String codeMainNm,
			String codeSubNm, String workDetail, String signStatus, Double breakTime, String overtimeDetail, String wfhYn,
			String isHoliday, Long memberId, String isDeadline) {
		this.perfId = perfId;
		this.seq = seq;
		this.taskHour = taskHour;
		this.perfDay = perfDay;
		this.dayHour = dayHour;
		this.startedHour = startedHour;
		this.endedHour = endedHour;
		this.groupMainId = groupMainId;
		this.groupSubId = groupSubId;
		this.codeId = codeId;
		this.codeMainNm = codeMainNm;
		this.codeSubNm = codeSubNm;
		this.workDetail = workDetail;
		this.signStatus = signStatus;
		this.breakTime = breakTime;
		this.overtimeDetail = overtimeDetail;
		this.wfhYn = wfhYn;
		this.isHoliday = isHoliday;
		this.memberId = memberId;
		this.isDeadline = isDeadline;
	}


	public static PerformanceDto entityToDto(Performance p, String isHoliday, String isDeadline) {
		return PerformanceDto.builder()
				.perfId(p.getPerfId())
				.seq(p.getSeq())
				.taskHour(p.getTaskHour())
				.perfDay(Integer.parseInt(p.getPerfDay()))
				.dayHour(p.getDayHour())
				.startedHour(p.getStartedHour())
				.endedHour(p.getEndedHour())
				.groupMainId(p.getGroupMainId())
				.groupSubId(p.getGroupSubId())
				.codeId(p.getCodeId())
				.codeMainNm(p.getCodeMainNm())
				.codeSubNm(p.getCodeSubNm())
				.workDetail(p.getWorkDetail())
				.signStatus(p.getSignStatus())
				.breakTime(p.getBreakTime())
				.overtimeDetail(p.getOvertimeDetail())
				.wfhYn(p.getWfhYn())
				.isHoliday(isHoliday)
				.memberId(p.getMember().getMemberId())
				.isDeadline(isDeadline)
				.build();
	}

	public static PerformanceDto dummyDto(String perfDay, String isHoliday) {
		return PerformanceDto.builder()
				.perfId(0L)
				.seq(0)
				.taskHour(0.0)
				.perfDay(Integer.valueOf(perfDay))
				.dayHour(0.0)
				.startedHour("")
				.endedHour("")
				.groupMainId("")
				.groupSubId("")
				.codeId("")
				.codeMainNm("")
				.codeSubNm("")
				.workDetail("")
				.signStatus("")
				.breakTime(0.0)
				.overtimeDetail("")
				.wfhYn("")
				.isHoliday(isHoliday)
				.memberId(0L)
				.isDeadline("0")
				.build();

	}
}
