package com.hjun.timereport.performance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PerformanceSaveReq {

	private int seq;

	private Double taskHour;

	private String perfDay;

	private Double dayHour;

	private String startedHour;

	private String endedHour;

	private String groupMainId;

	private String groupSubId;

	private String codeId;

	private String codeMainNm;

	private String codeSubNm;

	private String workDetail;

	private String wfhYn;

	private Double breakTime;

	private String signStatus;

	private String overtimeDetail;

	private Long memberId;


	public PerformanceSaveReq() {
	}


	@Builder
	public PerformanceSaveReq(int seq, Double taskHour, String perfDay, Double dayHour, String startedHour,
			String endedHour, String groupMainId, String groupSubId, String codeId, String codeMainNm, String codeSubNm,
			String workDetail, String wfhYn, Double breakTime, String signStatus,
			String overtimeDetail, Long memberId) {
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
		this.wfhYn = wfhYn;
		this.breakTime = breakTime;
		this.signStatus = signStatus;
		this.overtimeDetail = overtimeDetail;
		this.memberId = memberId;
	}


}
