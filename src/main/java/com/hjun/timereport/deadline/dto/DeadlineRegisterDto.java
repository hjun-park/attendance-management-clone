package com.hjun.timereport.deadline.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeadlineRegisterDto {

	private List<String> days;

	@Builder
	public DeadlineRegisterDto(List<String> days) {
		this.days = days;
	}



}
