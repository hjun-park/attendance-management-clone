package com.hjun.timereport.performance.dto;

import com.hjun.timereport.performance.entity.Performance;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceProjectDto {

	private Long perfId;

	private int seq;

	private Double taskHour;

	private String groupMainId;

	private String groupSubId;

	private String codeId;

	private String codeMainNm;

	private String codeSubNm;

	private String workDetail;

	@Builder
	public PerformanceProjectDto(Long perfId, int seq, Double taskHour, String groupMainId, String groupSubId,
			String codeId, String codeMainNm, String codeSubNm, String workDetail) {
		this.perfId = perfId;
		this.seq = seq;
		this.taskHour = taskHour;
		this.groupMainId = groupMainId;
		this.groupSubId = groupSubId;
		this.codeId = codeId;
		this.codeMainNm = codeMainNm;
		this.codeSubNm = codeSubNm;
		this.workDetail = workDetail;
	}

	public PerformanceProjectDto EntityToDto(Performance performance) {
		return PerformanceProjectDto.builder()
				.perfId(performance.getPerfId())
				.seq(performance.getSeq())
				.taskHour(performance.getTaskHour())
				.groupMainId(performance.getGroupMainId())
				.groupSubId(performance.getGroupSubId())
				.codeId(performance.getCodeId())
				.codeMainNm(performance.getCodeMainNm())
				.codeSubNm(performance.getCodeSubNm())
				.workDetail(performance.getWorkDetail())
				.build();
	}



}
