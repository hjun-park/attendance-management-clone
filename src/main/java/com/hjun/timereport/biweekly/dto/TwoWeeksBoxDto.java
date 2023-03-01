package com.hjun.timereport.biweekly.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TwoWeeksBoxDto {

	private List<TwoWeeksDto> twoWeeksDtos;
	private String startOfWeek;

	@Builder
	public TwoWeeksBoxDto(List<TwoWeeksDto> twoWeeksDtos, String startOfWeek) {
		this.twoWeeksDtos = twoWeeksDtos;
		this.startOfWeek = startOfWeek;
	}

}
