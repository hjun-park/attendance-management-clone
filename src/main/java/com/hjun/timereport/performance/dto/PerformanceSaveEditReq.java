package com.hjun.timereport.performance.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PerformanceSaveEditReq {

	@Positive
	private Double breakTime;

	@NotBlank
	private String startedHour;

	@NotBlank
	private String endedHour;

	@Positive
	private Double dayHour;

	private String overtimeDetail;

	@NotBlank
	private String signStatus;

	@NotBlank
	private String wfhYn;

	@Positive
	private Long perfId;

	@NotBlank
	private String groupMainId;

	@NotBlank
	private String groupSubId;

	@NotBlank
	private String codeId;

	@NotBlank
	private String codeMainNm;

	@NotBlank
	private String codeSubNm;

	@NotBlank
	private String workDetail;

	@Positive
	private Double taskHour;

	@Positive
	private int seq;

	public PerformanceSaveEditReq() {
	}

	@Builder
	public PerformanceSaveEditReq(Double breakTime, String startedHour, String endedHour, Double dayHour,
			String overtimeDetail, String signStatus, String wfhYn, Long perfId, String groupMainId,
			String groupSubId, String codeId, String codeMainNm, String codeSubNm, String workDetail, Double taskHour,
			int seq) {
		this.breakTime = breakTime;
		this.startedHour = startedHour;
		this.endedHour = endedHour;
		this.dayHour = dayHour;
		this.overtimeDetail = overtimeDetail;
		this.signStatus = signStatus;
		this.wfhYn = wfhYn;
		this.perfId = perfId;
		this.groupMainId = groupMainId;
		this.groupSubId = groupSubId;
		this.codeId = codeId;
		this.codeMainNm = codeMainNm;
		this.codeSubNm = codeSubNm;
		this.workDetail = workDetail;
		this.taskHour = taskHour;
		this.seq = seq;
	}

}
