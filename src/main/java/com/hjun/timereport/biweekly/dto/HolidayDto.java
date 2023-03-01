package com.hjun.timereport.biweekly.dto;

import com.hjun.timereport.biweekly.entity.Holiday;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HolidayDto {

	private String holidayDate;

	private Integer dayOfTheWeek;

	private Integer week;

	private String lunaDay;

	private String solarName;

	private String lunaName;

	private String isHoliday;

	private String holidayTitle;

	@Builder
	public HolidayDto(String holidayDate, Integer dayOfTheWeek, Integer week, String lunaDay, String solarName,
			String lunaName, String isHoliday, String holidayTitle) {
		this.holidayDate = holidayDate;
		this.dayOfTheWeek = dayOfTheWeek;
		this.week = week;
		this.lunaDay = lunaDay;
		this.solarName = solarName;
		this.lunaName = lunaName;
		this.isHoliday = isHoliday;
		this.holidayTitle = holidayTitle;
	}

	public static HolidayDto entityToDto(Holiday h) {
		return HolidayDto.builder().holidayDate(h.getHolidayDate()).dayOfTheWeek(h.getDayOfTheWeek()).week(h.getWeek())
				.lunaDay(h.getLunaDay()).solarName(h.getSolarName()).lunaName(h.getLunaName())
				.isHoliday(h.getIsHoliday()).holidayTitle(h.getHolidayTitle()).build();
	}

}
