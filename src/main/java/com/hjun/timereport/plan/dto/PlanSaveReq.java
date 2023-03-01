package com.hjun.timereport.plan.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlanSaveReq {

	@NotBlank
	private String planDay;

	@Positive
	private Long planId;

	@NotBlank
	private String startedHour;

	@NotBlank
	private String endedHour;

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
	private Integer seq;

	@Positive
	private Double taskHour;

	@NotBlank
	private String wfhYn;

	public PlanSaveReq() {
	}

	@Builder
	public PlanSaveReq(String planDay, Long planId, String startedHour, String endedHour, String groupMainId,
			String groupSubId, String codeId, String codeMainNm, String codeSubNm, String workDetail, Integer seq,
			Double taskHour, String wfhYn) {
		this.planDay = planDay;
		this.planId = planId;
		this.startedHour = startedHour;
		this.endedHour = endedHour;
		this.groupMainId = groupMainId;
		this.groupSubId = groupSubId;
		this.codeId = codeId;
		this.codeMainNm = codeMainNm;
		this.codeSubNm = codeSubNm;
		this.workDetail = workDetail;
		this.seq = seq;
		this.taskHour = taskHour;
		this.wfhYn = wfhYn;
	}








}
