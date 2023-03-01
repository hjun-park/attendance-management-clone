package com.hjun.timereport.deadline.dto;

import com.hjun.timereport.deadline.entity.Deadline;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeadlineDto {

	private String isDeadline;
	private Integer deadlineDay;
	private Integer dayOfTheWeek;

	@Builder
	public DeadlineDto(String isDeadline, Integer deadlineDay, Integer dayOfTheWeek) {
		this.isDeadline = isDeadline;
		this.deadlineDay = deadlineDay;
		this.dayOfTheWeek = dayOfTheWeek;
	}

	public static DeadlineDto of(Deadline deadline, Integer dayOfTheWeek) {
		return DeadlineDto.builder()
				.isDeadline(deadline.getIsDeadline())
				.deadlineDay(Integer.parseInt(deadline.getDeadlineDay()))
				.dayOfTheWeek(dayOfTheWeek)
				.build();
	}



}



