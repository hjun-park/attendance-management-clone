package com.hjun.timereport.plan.dto;

import com.hjun.timereport.plan.entity.Plan;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanDto {

	private Long planId;
	private Integer seq;
	private Double taskHour;
	private Integer planDay;
	private Double dayHour;
	private String startedHour;
	private String endedHour;

	private String groupMainId;
	private String groupSubId;
	private String codeId;
	private String codeMainNm;
	private String codeSubNm;
	private String workDetail;
	private String enrollYn;

	private String wfhYn;
	private String isHoliday;
	private Long memberId;

	@Builder
	public PlanDto(Long planId, Integer seq, Double taskHour, Integer planDay, Double dayHour, String startedHour,
			String endedHour, String groupMainId, String groupSubId, String codeId, String codeMainNm, String codeSubNm,
			String workDetail, String enrollYn, String wfhYn, String isHoliday, Long memberId) {
		this.planId = planId;
		this.seq = seq;
		this.taskHour = taskHour;
		this.planDay = planDay;
		this.dayHour = dayHour;
		this.startedHour = startedHour;
		this.endedHour = endedHour;
		this.groupMainId = groupMainId;
		this.groupSubId = groupSubId;
		this.codeId = codeId;
		this.codeMainNm = codeMainNm;
		this.codeSubNm = codeSubNm;
		this.workDetail = workDetail;
		this.enrollYn = enrollYn;
		this.wfhYn = wfhYn;
		this.isHoliday = isHoliday;
		this.memberId = memberId;
	}

	public static PlanDto entityToDto(Plan p, String isHoliday) {
		return PlanDto.builder()
				.planId(p.getPlanId())
				.seq(p.getSeq())
				.taskHour(p.getTaskHour())
				.planDay(Integer.parseInt(p.getPlanDay()))
				.dayHour(p.getDayHour())
				.startedHour(p.getStartedHour())
				.endedHour(p.getEndedHour())
				.groupMainId(p.getGroupMainId())
				.groupSubId(p.getGroupSubId())
				.codeId(p.getCodeId())
				.codeMainNm(p.getCodeMainNm())
				.codeSubNm(p.getCodeSubNm())
				.workDetail(p.getWorkDetail())
				.enrollYn(p.getEnrollYn())
				.wfhYn(p.getWfhYn())
				.isHoliday(isHoliday)
				.memberId(p.getMember().getMemberId())
				.build();
	}

	public static PlanDto dummyDto(String planDay, String isHoliday) {
		return PlanDto.builder()
				.planId(0L)
				.seq(0)
				.taskHour(0.0)
				.planDay(Integer.valueOf(planDay))
				.dayHour(0.0)
				.startedHour("")
				.endedHour("")
				.groupMainId("")
				.groupSubId("")
				.codeId("")
				.codeMainNm("")
				.codeSubNm("")
				.workDetail("")
				.enrollYn("")
				.wfhYn("")
				.isHoliday(isHoliday)
				.memberId(0L)
				.build();
	}



}
