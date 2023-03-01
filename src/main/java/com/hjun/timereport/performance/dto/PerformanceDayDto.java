package com.hjun.timereport.performance.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceDayDto {

	private String perfDay;
	private String startedHour;
	private String endedHour;
	private String signStatus;
	private Double dayHour;
	private Double breakTime;
	private String overtimeDetail;
	private String wfhYn;
	private List<PerformanceProjectDto> projectDtoList;
	private List<PerformanceTaskDto> taskDtoList;

	@Builder
	public PerformanceDayDto(String perfDay, String startedHour, String endedHour, String signStatus, Double dayHour,
			Double breakTime, String overtimeDetail, String wfhYn, List<PerformanceProjectDto> projectDtoList,
			List<PerformanceTaskDto> taskDtoList) {
		this.perfDay = perfDay;
		this.startedHour = startedHour;
		this.endedHour = endedHour;
		this.signStatus = signStatus;
		this.dayHour = dayHour;
		this.breakTime = breakTime;
		this.overtimeDetail = overtimeDetail;
		this.wfhYn = wfhYn;
		this.projectDtoList = projectDtoList;
		this.taskDtoList = taskDtoList;
	}


}
